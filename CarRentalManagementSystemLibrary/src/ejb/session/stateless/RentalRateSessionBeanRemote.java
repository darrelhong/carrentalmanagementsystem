package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.util.List;
import util.exception.CarCategoryNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;


public interface RentalRateSessionBeanRemote {

    RentalRate createRentalRate(RentalRate newRentalRate, Long carCategoryId) throws UnknownPersistenceException, CarCategoryNotFoundException;

    RentalRate createRentalRate(RentalRate newRentalRate, CarCategory carCategory) throws UnknownPersistenceException;

    List retreiveAllRentalRates();

    RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException;

    RentalRate updateRate(RentalRate newRentalRate, Long carCategoryId) throws CarCategoryNotFoundException;

    Integer deleteRate(Long rateId) throws RentalRateNotFoundException;
    
}
