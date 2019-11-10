package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
import java.util.List;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.UnknownPersistenceException;

public interface CarModelSessionBeanLocal {

    CarModel createNewCarModel(CarModel newCarModel, CarCategory carCategory) throws UnknownPersistenceException;

    CarModel createNewCarModel(CarModel newCarModel, Long categoryId) throws UnknownPersistenceException, CarCategoryNotFoundException;

    List retrieveAllCarModels();

    CarModel updateCarModel(CarModel updatedCarModel, Long modelId, Long categoryId) throws CarCategoryNotFoundException, CarModelNotFoundException;

    Integer deleteCarModel(Long modelId) throws CarModelNotFoundException;

    List retrieveCarModelsByCategory(Long categoryId);

    CarModel retrieveCarModelById(Long modelId) throws CarModelNotFoundException;
}
