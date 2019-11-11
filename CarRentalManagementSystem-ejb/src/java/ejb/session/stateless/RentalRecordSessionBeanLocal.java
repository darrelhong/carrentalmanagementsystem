package ejb.session.stateless;

import entity.Customer;
import entity.Employee;
import entity.RentalRecord;
import entity.TransitDispatchRecord;
import java.util.List;
import util.exception.CancellationErrorException;
import util.exception.CarNotAssignedException;
import util.exception.CustomerNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.RentalRecordNotFoundException;
import util.exception.TransitDispatchRecordNotFoundException;
import util.exception.TransitNotAssignedException;

/**
 *
 * @author darre
 */
public interface RentalRecordSessionBeanLocal {

    List retrieveAllCustomerReservations(Customer customer) throws CustomerNotFoundException;

    RentalRecord retrieveRentalRecordById(Long bookingId) throws RentalRecordNotFoundException;

    String cancelReservation(Long recordId) throws CancellationErrorException;

    void allocateCars();

    List retrieveTransitDispatchRecords();

    List retrieveCurrentDayDispatchRecords(Employee employee);

    TransitDispatchRecord assignEmployeeToTDR(Long tdrId, Long employeeId) throws EmployeeNotFoundException, TransitDispatchRecordNotFoundException;

    TransitDispatchRecord markTransitAsCompleted(Long tdrId) throws TransitDispatchRecordNotFoundException, TransitNotAssignedException;

    RentalRecord pickupCar(Long rentalRecordId) throws RentalRecordNotFoundException, CarNotAssignedException;

    RentalRecord returnCar(Long rentalRecordId) throws RentalRecordNotFoundException;

    List retrieveAllPartnerReservations(java.lang.String partnerId) throws PartnerNotFoundException;
    
}
