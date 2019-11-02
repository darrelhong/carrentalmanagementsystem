package ejb.session.stateful;

import entity.Customer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InsufficientInventoryException;
import util.exception.NoRateFoundException;
import util.exception.OutletClosedException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;


public interface BookingSessionBeanRemote {

    BigDecimal searchByCarModel(Long categoryId, Long modelId, Date start, Date end, Long pickupOutletId, Long returnOutletId) throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, OutletClosedException;

    List searchByCarCategory(Long categoryId, Date start, Date end, Long pickupOutletId, Long returnOutletId) throws CarCategoryNotFoundException, InsufficientInventoryException, NoRateFoundException;

    void confirmReservation(Customer customer, String ccNum, boolean immediatePayment) throws CustomerNotFoundException, UnknownPersistenceException;
    
}
