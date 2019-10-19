package ejb.session.stateless;

import entity.CarCategory;
import util.exception.UnknownPersistenceException;

public interface CarCategorySessionBeanRemote {

    CarCategory createNewCarCategory(CarCategory newCarCategory) throws UnknownPersistenceException;
    
}
