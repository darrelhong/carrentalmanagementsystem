package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import entity.RentalRecord;
import entity.TransitDispatchRecord;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CancellationErrorException;
import util.exception.CarNotAssignedException;
import util.exception.CustomerNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.RentalRecordNotFoundException;
import util.exception.TransitDispatchRecordNotFoundException;
import util.exception.TransitNotAssignedException;
import util.helper.Print;

/**
 *
 * @author darre
 */
@Stateless
@Local(RentalRecordSessionBeanLocal.class)
@Remote(RentalRecordSessionBeanRemote.class)
public class RentalRecordSessionBean implements RentalRecordSessionBeanRemote, RentalRecordSessionBeanLocal {

    @EJB(name = "PartnerSessionBeanLocal")
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public RentalRecordSessionBean() {
    }

    @Override
    public List retrieveAllCustomerReservations(Customer customer) throws CustomerNotFoundException {
        Customer cust = customerSessionBeanLocal.retrieveCustomerByCustomerId(customer.getCustomerId());
        cust.getRentalRecords().size();
        return cust.getRentalRecords();
    }

    @Override
    public RentalRecord retrieveRentalRecordById(Long bookingId) throws RentalRecordNotFoundException {
        RentalRecord record = em.find(RentalRecord.class, bookingId);
        if (record != null) {
            return record;
        } else {
            throw new RentalRecordNotFoundException("Booking with ID " + bookingId + " not found!");
        }
    }

    @Override
    public String cancelReservation(Long recordId) throws CancellationErrorException {
        RentalRecord toCancel = em.find(RentalRecord.class, recordId);

        if (toCancel.getStartDateTime().before(new Date())) {
            throw new CancellationErrorException("Cannot cancel after rental period has started!");
        }
        
        BigDecimal penalty = calculatePenalty(toCancel);
        toCancel.setPenaltyAmount(penalty);
        toCancel.setCancelled(true);
        if (toCancel.getPaid()) {
            BigDecimal refundAmount = toCancel.getAmount().subtract(penalty);
            System.out.println(refundAmount);
            toCancel.setAmountToRefund(refundAmount);
            return String.format("Reservation cancelled. Amount to refund: %8.2f", refundAmount);
        } else {
            return String.format("Reservation cancelled. Penalty charged: %8.2f", penalty);
        }
    }

    private BigDecimal calculatePenalty(RentalRecord toCancel) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
            Date now = sdf.parse("1/1/2019 12:00 AM");
            BigDecimal result = new BigDecimal(0);
            long timeDiff = toCancel.getStartDateTime().getTime() - now.getTime();
            if (timeDiff > 14 * 24 * 3600 * 1000) {
                System.out.println("No penalty");
                System.out.println("penalty amount " + result);
                return result;
            }
            if (timeDiff > 7 * 24 * 3600 * 1000) {
                System.out.println("0.2 penalty");
                result = toCancel.getAmount().multiply(new BigDecimal(0.2));
                System.out.println("penalty amount " + result);
                return result;
            }
            if (timeDiff > 3 * 24 * 3600 * 1000) {
                result = toCancel.getAmount().multiply(new BigDecimal(0.5));
                System.out.println("0.5 penalty");
                System.out.println("penalty amount " + result);
                return result;
            }
            result = toCancel.getAmount().multiply(new BigDecimal(0.7));
            System.out.println("0.7 penalty");
            System.out.println("penalty amount " + result);
            return result;
        } catch (ParseException ex) {
            System.out.println("SimpleDateFormat ParseException");
            return null;
        }
    }

    @Override
    public void allocateCars() {
//        Date now = new Date();
        Date now = new Date(119, 0, 1, 0, 0);
        Date endOfDay = new Date(now.getTime() + 24 * 3600 * 1000);
        System.out.println(now);
        System.out.println(endOfDay);
        Query q = em.createQuery("SELECT rr FROM RentalRecord rr WHERE rr.cancelled = FALSE AND rr.startDateTime BETWEEN :now AND :endOfDay ORDER BY rr.carModel DESC");
        q.setParameter("now", now);
        q.setParameter("endOfDay", endOfDay);

        List<RentalRecord> todayRecords = q.getResultList();
        Print.printListRentalRecords(todayRecords);
        for (RentalRecord rr : todayRecords) {
            Outlet pickupFrom = rr.getFromOutlet();
            // assign bookings with specific model
            if (rr.getCarModel() != null) {
                CarModel carModel = rr.getCarModel();
                Query qCar = em.createQuery("SELECT c FROM Car c WHERE c.availabilityStatus = TRUE AND c.disabled = FALSE AND c.carModel = :rrCarModel AND c.outlet = :rrOutlet");
                qCar.setParameter("rrOutlet", pickupFrom);
                qCar.setParameter("rrCarModel", carModel);
                List<Car> carResult = qCar.setMaxResults(1).getResultList();
                if (!carResult.isEmpty()) {
                    Car car = carResult.get(0);
                    Print.printCar(car);
                    car.setAvailabilityStatus(false);
                    rr.setCar(car);
                    car.getRentalRecords().add(rr);
                    // TRANSIT
                } else {
                    System.out.println("TRANSIT");
                    Query qCar2 = em.createQuery("SELECT c FROM Car c WHERE c.availabilityStatus = TRUE AND c.disabled = FALSE AND c.carModel = :rrCarModel");
                    qCar2.setParameter("rrCarModel", carModel);
                    List<Car> carResult2 = qCar2.setMaxResults(1).getResultList();
                    if (!carResult2.isEmpty()) {
                        Car car = carResult2.get(0);
                        Print.printCar(car);
                        car.setAvailabilityStatus(false);
                        rr.setCar(car);
                        car.getRentalRecords().add(rr);
                        generateTransitRecord(rr, car);
                    } else {
                        System.out.println("Error no suitable car available");
                    }
                }
                //assign bookings without specific category
            } else {
                CarCategory carCategory = rr.getCarCategory();
                Query qCar = em.createQuery("SELECT c FROM Car c WHERE c.availabilityStatus = TRUE AND c.disabled = FALSE AND c.carCategory = :rrCarCategory AND c.outlet = :rrOutlet");
                qCar.setParameter("rrOutlet", pickupFrom);
                qCar.setParameter("rrCarCategory", carCategory);
                List<Car> carResult = qCar.setMaxResults(1).getResultList();
                if (!carResult.isEmpty()) {
                    Car car = carResult.get(0);
                    Print.printCar(car);
                    car.setAvailabilityStatus(false);
                    rr.setCar(car);
                    car.getRentalRecords().add(rr);
                    // TRANSIT
                } else {
                    System.out.println("TRANSIT");
                    Query qCar2 = em.createQuery("SELECT c FROM Car c WHERE c.availabilityStatus = TRUE AND c.disabled = FALSE AND  c.carCategory = :rrCarCategory");
                    qCar2.setParameter("rrCarCategory", carCategory);
                    List<Car> carResult2 = qCar2.setMaxResults(1).getResultList();
                    if (!carResult2.isEmpty()) {
                        Car car = carResult2.get(0);
                        Print.printCar(car);
                        car.setAvailabilityStatus(false);
                        rr.setCar(car);
                        car.getRentalRecords().add(rr);
                        generateTransitRecord(rr, car);
                    } else {
                        System.out.println("Error no suitable cars available");
                    }
                }
            }
        }
    }

    private void generateTransitRecord(RentalRecord rentalRecord, Car car) {
        TransitDispatchRecord tdr = new TransitDispatchRecord();
        em.persist(tdr);
        tdr.setRentalRecord(rentalRecord);
        tdr.setFromOutlet(car.getOutlet());
    }

    @Override
    public List retrieveTransitDispatchRecords() {
//        Date now = new Date();
        Date now = new Date(119, 0, 1, 0, 0);
        Date startOfDay = new Date(now.getYear(), now.getMonth(), now.getDate());
        Date endOfDay = new Date(now.getTime() + 24 * 3600 * 1000);

        Query q = em.createQuery("SELECT tdr FROM TransitDispatchRecord tdr WHERE tdr.rentalRecord.startDateTime BETWEEN :startOfDay AND :endOfDay");
        q.setParameter("startOfDay", startOfDay);
        q.setParameter("endOfDay", endOfDay);

        List<TransitDispatchRecord> tdrs = q.getResultList();
        Print.printListTDR(tdrs);
        return tdrs;
    }

    @Override
    public List retrieveCurrentDayDispatchRecords(Employee employee) {
//        Date now = new Date();
        Date now = new Date(119, 0, 1, 0, 0);
        Date startOfDay = new Date(now.getYear(), now.getMonth(), now.getDate());
        Date endOfDay = new Date(now.getTime() + 24 * 3600 * 1000);

        Query q = em.createQuery("SELECT tdr FROM TransitDispatchRecord tdr WHERE tdr.rentalRecord.startDateTime BETWEEN :startOfDay AND :endOfDay AND tdr.rentalRecord.fromOutlet = :employeeOutlet");
        q.setParameter("startOfDay", startOfDay);
        q.setParameter("endOfDay", endOfDay);
        q.setParameter("employeeOutlet", employee.getOutlet());

        List<TransitDispatchRecord> tdrs = q.getResultList();
        return tdrs;
    }

    @Override
    public TransitDispatchRecord assignEmployeeToTDR(Long tdrId, Long employeeId) throws EmployeeNotFoundException, TransitDispatchRecordNotFoundException {
        Employee employee = employeeSessionBeanLocal.retrieveEmployeeById(employeeId);
        TransitDispatchRecord tdr = em.find(TransitDispatchRecord.class, tdrId);
        if (tdr == null) {
            throw new TransitDispatchRecordNotFoundException("Transit dispatch record with ID " + tdrId + " does not exist!");
        }
        tdr.setEmployee(employee);
        return tdr;
    }

    @Override
    public TransitDispatchRecord markTransitAsCompleted(Long tdrId) throws TransitDispatchRecordNotFoundException, TransitNotAssignedException {
        TransitDispatchRecord tdr = em.find(TransitDispatchRecord.class, tdrId);
        if (tdr == null) {
            throw new TransitDispatchRecordNotFoundException("Transit dispatch record with ID " + tdrId + " does not exist!");
        }
        if (tdr.getEmployee() == null) {
            throw new TransitNotAssignedException("Transit dispatch record with ID " + tdrId + " has not been assigned an employee!");
        }
        // Set new car location
        Car car = tdr.getRentalRecord().getCar();
        Outlet source = tdr.getFromOutlet();
        Outlet destination = tdr.getRentalRecord().getFromOutlet();
        car.setOutlet(destination);
        source.getCars().remove(car);
        destination.getCars().add(car);
        tdr.setCompleted(true);
        return tdr;
    }

    @Override
    public RentalRecord pickupCar(Long rentalRecordId) throws RentalRecordNotFoundException, CarNotAssignedException {
        RentalRecord rr = retrieveRentalRecordById(rentalRecordId);
        if (rr.getCar() == null) {
            throw new CarNotAssignedException("Booking has not been assigned a car!");
        }
        if (!rr.getPaid()) {
            rr.setPaid(true);
        }
        rr.setPickedUp(true);
        return rr;
    }

    @Override
    public RentalRecord returnCar(Long rentalRecordId) throws RentalRecordNotFoundException {
        RentalRecord rr = retrieveRentalRecordById(rentalRecordId);
        Car car = rr.getCar();
        Outlet source = rr.getFromOutlet();
        Outlet destination = rr.getToOutlet();
        if (source != destination) {
            car.setOutlet(destination);
            car.setAvailabilityStatus(true);
            source.getCars().remove(car);
            destination.getCars().add(car);
        }
        return rr;
    }

    @Override
    public List retrieveAllPartnerReservations(java.lang.String partnerId) throws PartnerNotFoundException {
        Partner partner = partnerSessionBeanLocal.retrievePartnerByUsername(partnerId);
        partner.getRentalRecords().size();
        return partner.getRentalRecords();
    }
}
