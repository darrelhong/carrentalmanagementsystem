package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import util.exception.UnknownPersistenceException;

public interface CarSessionBeanRemote {

    Car createNewCar(Car newCar, CarCategory carCategory, CarModel carModel) throws UnknownPersistenceException;
    
}
