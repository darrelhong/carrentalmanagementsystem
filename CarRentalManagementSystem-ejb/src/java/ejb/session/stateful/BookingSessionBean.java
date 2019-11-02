package ejb.session.stateful;

import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Outlet;
import entity.RentalRate;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

/**
 *
 * @author darre
 */
@Stateful
@Local(BookingSessionBeanLocal.class)
@Remote(BookingSessionBeanRemote.class)
public class BookingSessionBean implements BookingSessionBeanRemote, BookingSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    CarModel bookingCarModel;
    Date bookingStartDate;
    Date bookingEndDate;
    Outlet bookingPickupOutlet;
    Outlet bookingReturnOutlet;
    Boolean transferRequired;
    BigDecimal totalRate;

    public BookingSessionBean() {
    }

    @Override
    public BigDecimal searchByCarModel(Long categoryId, Long modelId, Date start, Date end, Long pickupOutletId, Long returnOutletId)
            throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, NoRateFoundException, OutletClosedException {
        CarCategory cat = em.find(CarCategory.class, categoryId);
        if (cat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + categoryId + " does not exist!");
        }
        CarModel model = em.find(CarModel.class, modelId);
        if (model == null) {
            throw new CarModelNotFoundException("Car mode ID " + modelId + " does not exist!");
        }
        bookingCarModel = model;
        Outlet pickupOutlet = em.find(Outlet.class, pickupOutletId);
        if (pickupOutlet == null) {
            throw new OutletNotFoundException("Outlet ID " + pickupOutletId + " does not exist!");
        }
        if (start.getHours() < pickupOutlet.getOpenTime()) {
            throw new OutletClosedException("Outlet at " + pickupOutlet.getAddress() + " opens at " + String.format("%02d:00", pickupOutlet.getOpenTime()) + "H");
        }
        bookingPickupOutlet = pickupOutlet;
        Outlet returnOutlet = em.find(Outlet.class, returnOutletId);
        if (returnOutlet == null) {
            throw new OutletNotFoundException("Outlet ID " + returnOutletId + " does not exist!");
        }
        if (end.getHours() > returnOutlet.getCloseTime()) {
            throw new OutletClosedException("Outlet at " + pickupOutlet.getAddress() + " closes at " + String.format("%02d:00", pickupOutlet.getCloseTime()) + "H");
        }
        bookingReturnOutlet = returnOutlet;
        Query rrCountQuery = em.createQuery("SELECT COUNT(rr) FROM RentalRecord rr WHERE rr.carModel.carModelId = :modelId AND rr.startDateTime BETWEEN :start AND :end OR rr.endDateTime BETWEEN :start AND :end OR :start BETWEEN rr.startDateTime AND rr.endDateTime");
        rrCountQuery.setParameter("modelId", modelId);
        rrCountQuery.setParameter("start", start);
        rrCountQuery.setParameter("end", end);
        Long rrCount = (Long) rrCountQuery.getSingleResult();
        System.out.println("rental record count " + rrCount);

        Query modelCountAtOutletQuery = em.createQuery("SELECT COUNT(c) FROM Car c WHERE c.carModel.carModelId = :modelId AND c.disabled = FALSE AND c.outlet.outletId = :pickupOutletId");
        modelCountAtOutletQuery.setParameter("modelId", modelId);
        modelCountAtOutletQuery.setParameter("pickupOutletId", pickupOutletId);
        Long modelCountAtOutlet = (Long) modelCountAtOutletQuery.getSingleResult();

        if (modelCountAtOutlet > rrCount) {
            // enough inventory at outlet
            try {
                totalRate = new BigDecimal(0);
                calculateRate(start, end, categoryId);
                System.out.println("totalRate " + totalRate);
            } catch (NoRateFoundException ex) {
                throw ex;
            }
        } else {
            // not enough inventory at outlet need to recheck rentalrecords
            Date twoHrsBeforeStart = new Date(start.getTime() - 3600 * 1000);
            Query rrCount2Query = em.createQuery("SELECT COUNT(rr) FROM RentalRecord rr WHERE rr.carModel.carModelId = :modelId AND rr.startDateTime BETWEEN :start AND :end OR rr.endDateTime BETWEEN :start AND :end OR :start BETWEEN rr.startDateTime AND rr.endDateTime");
            rrCount2Query.setParameter("modelId", modelId);
            rrCount2Query.setParameter("start", twoHrsBeforeStart);
            rrCount2Query.setParameter("end", end);
            Long rrCount2 = (Long) rrCount2Query.getSingleResult();
            System.out.println(rrCount2);

            Integer totalModelCount = em.find(CarModel.class, modelId).getCars().size();
            System.out.println(totalModelCount);

            if (totalModelCount > rrCount2) {
                try {
                    totalRate = new BigDecimal(0);
                    calculateRate(start, end, categoryId);
                    System.out.println("totalRate " + totalRate);
                } catch (NoRateFoundException ex) {
                    throw ex;
                }
                transferRequired = true;
            } else {
                throw new InsufficientInventoryException("Insufficient inventory for this model.");
            }
        }
        bookingStartDate = start;
        bookingEndDate = end;
        return totalRate;
    }

    private void calculateRate(Date start, Date end, Long categoryId) throws NoRateFoundException {
        System.out.println("calculate rate");
        Date curr = new Date(start.getTime());
        while (true) {
            if (end.after(curr)) {
                Query rentalRateQuery = em.createQuery("SELECT r FROM RentalRate r WHERE r.carCategory.carCategoryId = :carCategoryId AND r.disabled = FALSE AND :curr BETWEEN r.startDate AND r.endDate ORDER BY r.rate ASC");
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
                if (end.getDay() == curr.getDay()) {
                    Query rentalRateQuery = em.createQuery("SELECT r FROM RentalRate r WHERE r.carCategory.carCategoryId = :carCategoryId AND r.disabled = FALSE AND :curr BETWEEN r.startDate AND r.endDate ORDER BY r.rate ASC");
                    rentalRateQuery.setParameter("carCategoryId", categoryId);
                    rentalRateQuery.setParameter("curr", curr);
                    List<RentalRate> rentalRate = rentalRateQuery.setMaxResults(1).getResultList();
                    if (rentalRate.isEmpty()) {
                        throw new NoRateFoundException("No rate found for " + curr);
                    } else {
                        totalRate = totalRate.add(rentalRate.get(0).getRate());
                    }
                }
                break;
            }
        }
    }
    @Override
    public void confirmReservation(Customer customer, String ccNum, boolean immediatePayment) throws CustomerNotFoundException, UnknownPersistenceException {
        Customer cust = em.find(Customer.class, customer.getCustomerId());
        if (cust == null) {
            throw new CustomerNotFoundException("Customer with ID " + customer.getCustomerId() + " does not exist!");
        }
        RentalRecord newRR = new RentalRecord();
        try {
            em.persist(newRR);
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
        newRR.setStartDateTime(bookingStartDate);
        newRR.setEndDateTime(bookingEndDate);
        newRR.setCcNum(ccNum);
        newRR.setAmount(totalRate);
        newRR.setPaid(immediatePayment);
        newRR.setFromOutlet(bookingPickupOutlet);
        newRR.setToOutlet(bookingReturnOutlet);
        newRR.setCustomer(customer);
        cust.getRentalRecords().add(newRR);
        newRR.setCarModel(bookingCarModel);
    }

    // For data init
    @Override
    public void createTestRentalRecord(RentalRecord rentalRecord) throws UnknownPersistenceException {
        try {
            em.persist(rentalRecord);
            em.flush();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public List searchByCarCategory(Long categoryId, Date start, Date end, Long pickupOutletId, Long returnOutletId) throws CarCategoryNotFoundException, InsufficientInventoryException, NoRateFoundException {
        return null;
    }


}
