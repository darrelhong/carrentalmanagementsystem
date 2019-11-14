package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import java.util.List;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelDisabledException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.EntityDisabledException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

public interface CarSessionBeanRemote {

    Car createNewCar(Car newCar, CarCategory carCategory, CarModel carModel, Outlet outlet) throws UnknownPersistenceException, InputDataValidationException;

    Car createNewCar(Car newCar, Long categoryId, Long modelId, Long outletId) throws UnknownPersistenceException, EntityDisabledException, CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, CarModelDisabledException, InputDataValidationException;

    List retrieveAllCars();

    Car retrieveCarById(Long carId) throws CarNotFoundException;

    Integer deleteCar(Long carId) throws CarNotFoundException;

    Car updateCar(Car car, Long categoryId, Long modelId, Long outletId) throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException;
    
}
