package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
import util.exception.UnknownPersistenceException;

public interface CarModelSessionBeanLocal {

    CarModel createNewCarModel(CarModel newCarModel, CarCategory carCategory) throws UnknownPersistenceException;
    
}
