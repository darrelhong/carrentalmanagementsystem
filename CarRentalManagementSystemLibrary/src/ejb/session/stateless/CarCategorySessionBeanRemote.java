package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import util.exception.CarCategoryNotFoundException;
import util.exception.UnknownPersistenceException;

public interface CarCategorySessionBeanRemote {

    CarCategory createNewCarCategory(CarCategory newCarCategory) throws UnknownPersistenceException;

    List retrieveAllCarCategories();

    CarCategory retrieveCarCategoryById(Long categoryId) throws CarCategoryNotFoundException;

    
}
