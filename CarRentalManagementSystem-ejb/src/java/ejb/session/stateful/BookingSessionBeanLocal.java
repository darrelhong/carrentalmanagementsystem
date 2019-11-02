package ejb.session.stateful;

import entity.Customer;
import entity.RentalRecord;
import util.exception.CustomerNotFoundException;
import util.exception.UnknownPersistenceException;

public interface BookingSessionBeanLocal {

    void createTestRentalRecord(RentalRecord rentalRecord) throws UnknownPersistenceException;

    void confirmReservation(Customer customer, String ccNum, boolean immediatePayment) throws CustomerNotFoundException, UnknownPersistenceException;
    
}
