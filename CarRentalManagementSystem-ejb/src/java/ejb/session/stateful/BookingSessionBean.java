package ejb.session.stateful;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.ExternalCustomer;
import entity.Outlet;
import entity.Partner;
import entity.RentalRate;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InsufficientInventoryException;
import util.exception.NoRateFoundException;
import util.exception.OutletClosedException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

@Stateful
@Local(BookingSessionBeanLocal.class)
@Remote(BookingSessionBeanRemote.class)
public class BookingSessionBean implements BookingSessionBeanRemote, BookingSessionBeanLocal {

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB(name = "CarModelSessionBeanLocal")
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @EJB(name = "CarCategorySessionBeanLocal")
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    CarModel bookingCarModel;
    CarCategory bookingCarCatgory;
    Date bookingStartDate;
    Date bookingEndDate;
    Outlet bookingPickupOutlet;
    Outlet bookingReturnOutlet;
    Boolean transferRequired;
    BigDecimal totalRate;

    public BookingSessionBean() {
    }

    @Override
    public BigDecimal searchByCarCategory(Long categoryId, Date start, Date end, Long pickupOutletId, Long returnOutletId)
            throws CarCategoryNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, OutletClosedException {
        bookingCarModel = null;
        CarCategory category = carCategorySessionBeanLocal.retrieveCarCategoryById(categoryId);
        bookingCarCatgory = category;
        Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletId(pickupOutletId);
        if (pickupOutlet.getOpenTime() != null && start.getHours() < pickupOutlet.getOpenTime()) {
            throw new OutletClosedException("Outlet at " + pickupOutlet.getAddress() + " opens at " + String.format("%02d:00", pickupOutlet.getOpenTime()) + "H");
        }
        bookingPickupOutlet = pickupOutlet;
        Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletByOutletId(returnOutletId);
        if (returnOutlet.getCloseTime() != null && end.getHours() > returnOutlet.getCloseTime()) {
            throw new OutletClosedException("Outlet at " + pickupOutlet.getAddress() + " closes at " + String.format("%02d:00", pickupOutlet.getCloseTime()) + "H");
        }
        bookingReturnOutlet = returnOutlet;

        Query qClashBookings = em.createQuery("SELECT COUNT(rr) FROM RentalRecord rr WHERE rr.carCategory.carCategoryId = :categoryId AND rr.cancelled = FALSE AND (rr.startDateTime BETWEEN :start AND :end OR rr.endDateTime BETWEEN :start AND :end OR :start BETWEEN rr.startDateTime AND rr.endDateTime)");
        qClashBookings.setParameter("categoryId", categoryId);
        qClashBookings.setParameter("start", start);
        qClashBookings.setParameter("end", end);
        Long numClashBookings = (Long) qClashBookings.getSingleResult();
        System.out.println("numClashBookings " + numClashBookings);

        Query qCategoryTotalCount = em.createQuery("SELECT COUNT(c) FROM Car c WHERE c.carCategory.carCategoryId = :categoryId AND c.disabled = FALSE");
        qCategoryTotalCount.setParameter("categoryId", categoryId);
        Long categoryTotalCount = (Long) qCategoryTotalCount.getSingleResult();
        System.out.println("categoryTotalCount " + categoryTotalCount);

        Long currentAvail = categoryTotalCount - numClashBookings;
        System.out.println("currentAvail() " + currentAvail);
        if (currentAvail <= 0) {
            throw new InsufficientInventoryException("Insufficient inventory for this category: " + category.getCarCategory());
        }

        Date twoHrsBeforeStart = new Date(start.getTime() - 3600 * 1000);
        Query qClashBooking2HrsBefore = em.createQuery("SELECT COUNT(rr) FROM RentalRecord rr WHERE rr.carCategory.carCategoryId = :categoryId AND rr.endDateTime BETWEEN :twoHrsBefore AND :start AND rr.toOutlet.outletId <> :pickupOutletId");
        qClashBooking2HrsBefore.setParameter("categoryId", categoryId);
        qClashBooking2HrsBefore.setParameter("twoHrsBefore", twoHrsBeforeStart);
        qClashBooking2HrsBefore.setParameter("start", start);
        qClashBooking2HrsBefore.setParameter("pickupOutletId", pickupOutletId);
        Long clashBooking2HrsBefore = (Long) qClashBooking2HrsBefore.getSingleResult();
        System.out.println("clashBooking2HrsBefore " + clashBooking2HrsBefore);

        currentAvail -= clashBooking2HrsBefore;
        System.out.println("currentAvail(after check 2hrs) " + currentAvail);
        if (currentAvail <= 0) {
            throw new InsufficientInventoryException("Insufficient inventory for this category: " + category.getCarCategory());
        }
        totalRate = new BigDecimal(0);
        calculateRate(start, end, categoryId);
        System.out.println("totalRate " + totalRate);

        bookingStartDate = start;
        bookingEndDate = end;
        return totalRate;
    }

    @Override
    public BigDecimal searchByCarModel(Long categoryId, Long modelId, Date start, Date end, Long pickupOutletId, Long returnOutletId)
            throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, OutletClosedException {
        bookingCarCatgory = null;
        CarCategory category = carCategorySessionBeanLocal.retrieveCarCategoryById(categoryId);
        bookingCarCatgory = category;
        CarModel model = carModelSessionBeanLocal.retrieveCarModelById(modelId);
        bookingCarModel = model;
        Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletId(pickupOutletId);
        if (pickupOutlet.getOpenTime() != null && pickupOutlet.getCloseTime() != null && start.getHours() < pickupOutlet.getOpenTime() && start.getHours() > pickupOutlet.getCloseTime()) {
            throw new OutletClosedException("Outlet at " + pickupOutlet.getAddress() + " opens at " + String.format("%02d:00", pickupOutlet.getOpenTime()) + "H");
        }
        bookingPickupOutlet = pickupOutlet;
        Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletByOutletId(returnOutletId);
        if (returnOutlet.getCloseTime() != null && returnOutlet.getOpenTime() != null && end.getHours() > returnOutlet.getCloseTime() && end.getHours() < returnOutlet.getOpenTime()) {
            throw new OutletClosedException("Outlet at " + pickupOutlet.getAddress() + " closes at " + String.format("%02d:00", pickupOutlet.getCloseTime()) + "H");
        }
        bookingReturnOutlet = returnOutlet;

        Query qClashBookings = em.createQuery("SELECT COUNT(rr) FROM RentalRecord rr WHERE rr.carModel.carModelId = :modelId AND rr.cancelled = FALSE AND (rr.startDateTime BETWEEN :start AND :end OR rr.endDateTime BETWEEN :start AND :end OR :start BETWEEN rr.startDateTime AND rr.endDateTime)");
        qClashBookings.setParameter("modelId", modelId);
        qClashBookings.setParameter("start", start);
        qClashBookings.setParameter("end", end);
        Long numClashBookings = (Long) qClashBookings.getSingleResult();
        System.out.println("numClashBookings " + numClashBookings);

        Query qModelTotalCount = em.createQuery("SELECT COUNT(c) FROM Car c WHERE c.carModel.carModelId = :modelId AND c.disabled = FALSE");
        qModelTotalCount.setParameter("modelId", modelId);
        Long modelTotalCount = (Long) qModelTotalCount.getSingleResult();
        System.out.println("modelTotalCount" + modelTotalCount);

        Long currentAvail = modelTotalCount - numClashBookings;
        System.out.println("currentAvail()" + currentAvail);
        if (currentAvail <= 0) {
            throw new InsufficientInventoryException("Insufficient inventory for this model: " + model.getMake() + " " + model.getModel());
        }
        // Check for any rental record returning to another outlet 2hrs before
        Date twoHrsBeforeStart = new Date(start.getTime() - 3600 * 1000);
        Query qClashBooking2HrsBefore = em.createQuery("SELECT COUNT(rr) FROM RentalRecord rr WHERE rr.carModel.carModelId = :modelId AND rr.endDateTime BETWEEN :twoHrsBefore AND :start AND rr.toOutlet.outletId <> :pickupOutletId");
        qClashBooking2HrsBefore.setParameter("modelId", modelId);
        qClashBooking2HrsBefore.setParameter("twoHrsBefore", twoHrsBeforeStart);
        qClashBooking2HrsBefore.setParameter("start", start);
        qClashBooking2HrsBefore.setParameter("pickupOutletId", pickupOutletId);
        Long clashBooking2HrsBefore = (Long) qClashBooking2HrsBefore.getSingleResult();
        System.out.println("clashBooking2HrsBefore " + clashBooking2HrsBefore);

        currentAvail -= clashBooking2HrsBefore;
        System.out.println("currentAvail(after check 2hrs) " + currentAvail);
        if (currentAvail <= 0) {
            throw new InsufficientInventoryException("Insufficient inventory for this model: " + model.getMake() + " " + model.getModel());
        }
        totalRate = new BigDecimal(0);
        calculateRate(start, end, categoryId);
        System.out.println("totalRate " + totalRate);

        bookingStartDate = start;
        bookingEndDate = end;
        return totalRate;

    }

    private void calculateRate(Date start, Date end, Long categoryId) throws NoRateFoundException {
        System.out.println("calculate rate");
        Date curr = new Date(start.getTime());
        while (true) {
            if (end.after(curr)) {
                Query rentalRateQuery = em.createQuery("SELECT r FROM RentalRate r WHERE r.carCategory.carCategoryId = :carCategoryId AND r.disabled = FALSE AND (:curr BETWEEN r.startDate AND r.endDate OR (r.startDate IS NULL AND r.endDate IS NULL)) ORDER BY r.rate ASC");
                rentalRateQuery.setParameter("carCategoryId", categoryId);
                rentalRateQuery.setParameter("curr", curr);
                List<RentalRate> rentalRate = rentalRateQuery.setMaxResults(1).getResultList();
                if (rentalRate.isEmpty()) {
                    throw new NoRateFoundException("No rate found for " + curr);
                } else {
                    System.out.println("rental rate " + rentalRate.get(0).getRate());
                    totalRate = totalRate.add(rentalRate.get(0).getRate());
                }
                curr.setTime(curr.getTime() + 24 * 3600 * 1000);
            } else {
//                if (end.getDay() == curr.getDay()) {
//                    Query rentalRateQuery = em.createQuery("SELECT r FROM RentalRate r WHERE r.carCategory.carCategoryId = :carCategoryId AND r.disabled = FALSE AND (:curr BETWEEN r.startDate AND r.endDate OR (r.startDate IS NULL AND r.endDate IS NULL)) ORDER BY r.rate ASC");
//                    rentalRateQuery.setParameter("carCategoryId", categoryId);
//                    rentalRateQuery.setParameter("curr", curr);
//                    List<RentalRate> rentalRate = rentalRateQuery.setMaxResults(1).getResultList();
//                    if (rentalRate.isEmpty()) {
//                        throw new NoRateFoundException("No rate found for " + curr);
//                    } else {
//                        totalRate = totalRate.add(rentalRate.get(0).getRate());
//                    }
//                }
                break;
            }
        }
    }

    //ejb client
    @Override
    public void confirmReservation(Customer customer, String ccNum, boolean immediatePayment) throws CustomerNotFoundException, UnknownPersistenceException {
        Customer cust = em.find(Customer.class, customer.getCustomerId());
        if (cust == null) {
            throw new CustomerNotFoundException("Customer with ID " + customer.getCustomerId() + " does not exist!");
        }
        RentalRecord newRR = new RentalRecord();
        try {
            em.persist(newRR);
            newRR.setStartDateTime(bookingStartDate);
            newRR.setEndDateTime(bookingEndDate);
            newRR.setCcNum(ccNum);
            newRR.setAmount(totalRate);
            newRR.setPaid(immediatePayment);
            newRR.setFromOutlet(bookingPickupOutlet);
            newRR.setToOutlet(bookingReturnOutlet);
            newRR.setCustomer(customer);
            cust.getRentalRecords().add(newRR);
            if (bookingCarModel != null) {
                newRR.setCarModel(bookingCarModel);
                bookingCarModel.getRentalRecords().add(newRR);
            }
            if (bookingCarCatgory != null) {
                newRR.setCarCategory(bookingCarCatgory);
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public void confirmReservation(Long partnerId, String ccNum, Boolean immediatePayment, Long categoryId,
            Long modelId, Date start, Date end, Long pickupOutletId, Long returnOutletId, BigDecimal totalRate,
            String externalCustName) throws UnknownPersistenceException {
        RentalRecord rr = new RentalRecord();
        Partner partner = em.find(Partner.class, partnerId);
        Outlet pickupOutlet = em.find(Outlet.class, pickupOutletId);
        Outlet returnOutlet = em.find(Outlet.class, returnOutletId);
        CarCategory carCategory = em.find(CarCategory.class, categoryId);
        CarModel carModel;
        ExternalCustomer externalCustomer = new ExternalCustomer(externalCustName);
        try {
            em.persist(rr);
            em.persist(externalCustomer);
            rr.setStartDateTime(start);
            rr.setEndDateTime(end);
            rr.setCcNum(ccNum);
            rr.setAmount(totalRate);
            rr.setPaid(immediatePayment);
            rr.setFromOutlet(pickupOutlet);
            rr.setToOutlet(returnOutlet);
            rr.setPartner(partner);
            partner.getRentalRecords().add(rr);
            if (modelId == 0) {
                rr.setCarCategory(carCategory);
            } else {
                rr.setCarCategory(carCategory);
                carModel = em.find(CarModel.class, modelId);
                rr.setCarModel(carModel);
                carModel.getRentalRecords().add(rr);
            }
            rr.setExternalCustomer(externalCustomer);
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    // For data init
    @Override
    public void createTestRentalRecord(RentalRecord rentalRecord) throws UnknownPersistenceException {
        try {
            em.persist(rentalRecord);
            if (rentalRecord.getCarModel() != null) {
                rentalRecord.getCarModel().getRentalRecords().add(rentalRecord);
            }
            em.flush();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }
}
