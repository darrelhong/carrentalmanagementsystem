package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import util.exception.UnknownPersistenceException;

public interface CarCategorySessionBeanLocal {

    CarCategory createNewCarCategory(CarCategory newCarCategory) throws UnknownPersistenceException;

    List retrieveAllCarCategories();

    
}
