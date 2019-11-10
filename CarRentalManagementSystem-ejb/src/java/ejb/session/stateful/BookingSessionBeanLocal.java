package ejb.session.stateful;

import entity.Customer;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.util.Date;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InsufficientInventoryException;
import util.exception.NoRateFoundException;
import util.exception.OutletClosedException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

public interface BookingSessionBeanLocal {

    void createTestRentalRecord(RentalRecord rentalRecord) throws UnknownPersistenceException;

    void confirmReservation(Customer customer, String ccNum, boolean immediatePayment) throws CustomerNotFoundException, UnknownPersistenceException;

    public BigDecimal searchByCarCategory(Long categoryId, Date start, Date end, Long pickupOutletId, Long returnOutletId)
            throws CarCategoryNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, OutletClosedException;

    public BigDecimal searchByCarModel(Long categoryId, Long modelId, Date start, Date end, Long pickupOutletId, Long returnOutletId)
            throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, OutletClosedException;

    public void confirmReservation(Long partnerId, String ccNum, Boolean immediatePayment, Long categoryId, Long modelId, Date start, Date end, Long pickupOutletId, Long returnOutletId, BigDecimal totalRate, String externalCustName) throws UnknownPersistenceException;
}
