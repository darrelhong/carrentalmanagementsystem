package ejb.session.stateless;

import entity.Customer;
import entity.Employee;
import entity.RentalRecord;
import entity.TransitDispatchRecord;
import java.util.List;
import util.exception.CarNotAssignedException;
import util.exception.CustomerNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.RentalRecordNotFoundException;
import util.exception.TransitDispatchRecordNotFoundException;
import util.exception.TransitNotAssignedException;

/**
 *
 * @author darre
 */
public interface RentalRecordSessionBeanRemote {

    List retrieveAllCustomerReservations(Customer customer) throws CustomerNotFoundException;

    RentalRecord retrieveRentalRecordById(Long bookingId) throws RentalRecordNotFoundException;

    String cancelReservation(RentalRecord record);

    List retrieveTransitDispatchRecords();

    List retrieveCurrentDayDispatchRecords(Employee employee);

    TransitDispatchRecord assignEmployeeToTDR(Long tdrId, Long employeeId) throws EmployeeNotFoundException, TransitDispatchRecordNotFoundException;

    TransitDispatchRecord markTransitAsCompleted(Long tdrId) throws TransitDispatchRecordNotFoundException, TransitNotAssignedException;

    RentalRecord pickupCar(Long rentalRecordId) throws RentalRecordNotFoundException, CarNotAssignedException;

    RentalRecord returnCar(Long rentalRecordId) throws RentalRecordNotFoundException;

}
