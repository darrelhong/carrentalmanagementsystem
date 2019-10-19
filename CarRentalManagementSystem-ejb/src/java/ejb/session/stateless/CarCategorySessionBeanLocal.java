package ejb.session.stateless;

import entity.CarCategory;
import util.exception.UnknownPersistenceException;

public interface CarCategorySessionBeanLocal {

    CarCategory createNewCarCategory(CarCategory newCarCategory) throws UnknownPersistenceException;
    
}
