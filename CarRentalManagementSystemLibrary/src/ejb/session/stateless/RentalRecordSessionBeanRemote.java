package ejb.session.stateless;

import entity.Customer;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.util.List;
import util.exception.CancellationErrorException;
import util.exception.CustomerNotFoundException;
import util.exception.RentalRecordNotFoundException;

/**
 *
 * @author darre
 */
public interface RentalRecordSessionBeanRemote {

    List retrieveAllCustomerReservations(Customer customer) throws CustomerNotFoundException;

    RentalRecord retrieveRentalRecordById(Long bookingId) throws RentalRecordNotFoundException;

    String cancelReservation(RentalRecord record);

}
