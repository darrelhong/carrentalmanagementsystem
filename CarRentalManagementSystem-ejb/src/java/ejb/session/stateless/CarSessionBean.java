package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.EntityDisabledException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
@Stateless
@Local(CarSessionBeanLocal.class)
@Remote(CarSessionBeanRemote.class)
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public CarSessionBean() {
    }

    // For data initialisation
    @Override
    public Car createNewCar(Car newCar, CarCategory carCategory, CarModel carModel, Outlet outlet) throws UnknownPersistenceException {
        CarCategory cat = em.find(CarCategory.class, carCategory.getCarCategoryId());
        CarModel model = em.find(CarModel.class, carModel.getCarModelId());
        Outlet out = em.find(Outlet.class, outlet.getOutletId());
        try {
            em.persist(newCar);
            newCar.setCarCategory(cat);
            cat.getCars().add(newCar);
            newCar.setCarModel(model);
            model.getCars().add(newCar);
            newCar.setOutlet(out);
            out.getCars().add(newCar);
            em.flush();
            return newCar;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new car " + ex.getMessage());
        }
    }

    // For remote client
    @Override
    public Car createNewCar(Car newCar, Long categoryId, Long modelId, Long outletId) throws UnknownPersistenceException, EntityDisabledException, CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException {
        CarCategory cat = em.find(CarCategory.class, categoryId);
        if (cat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + categoryId + " does not exist!");
        }
        CarModel model = em.find(CarModel.class, modelId);
        if (model == null) {
            throw new CarModelNotFoundException("Car mode ID " + modelId + " does not exist!");
        }
        Outlet outlet = em.find(Outlet.class, outletId);
        if (outlet == null) {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
        if (model.getDisabled()) {
            throw new EntityDisabledException("Car model ID " + modelId + " is disabled!");
        }
        try {
            em.persist(newCar);
            newCar.setCarCategory(cat);
            cat.getCars().add(newCar);
            newCar.setCarModel(model);
            model.getCars().add(newCar);
            newCar.setOutlet(outlet);
            outlet.getCars().add(newCar);
            em.flush();
            return newCar;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new car " + ex.getMessage());
        }
    }

    @Override
    public List retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.disabled = FALSE ORDER BY c.carCategory, c.carModel.make, c.carModel.model, c.licensePlate");
        return query.getResultList();
    }

    @Override
    public Car retrieveCarById(Long carId) throws CarNotFoundException {
        Car car = em.find(Car.class, carId);
        if (car == null) {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
        return car;
    }

    @Override
    public Integer deleteCar(Long carId) throws CarNotFoundException {
        Car toDelete = em.find(Car.class, carId);
        if (toDelete == null) {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
        toDelete.getCarModel().getCars().remove(toDelete);
        toDelete.getCarCategory().getCars().remove(toDelete);
        // To edit when RentalRecord is implemented
        if (toDelete.getAvailabilityStatus()) {
            em.remove(toDelete);
            return 0;
        } else {
            toDelete.setDisabled(true);
            return 1;
        }
    }

    @Override
    public Car updateCar(Car car, Long carCategoryId, Long modelId, Long outletId) throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException {
        CarCategory newCat = em.find(CarCategory.class, carCategoryId);
        if (newCat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + carCategoryId + " does not exist!");
        }
//        newCat.getCars().size();
        CarModel newModel = em.find(CarModel.class, modelId);
        if (newModel == null) {
            throw new CarModelNotFoundException("Car model ID " + modelId + " does not exist!");
        }
//        newModel.getCars().size();
        Outlet newOutlet = em.find(Outlet.class, outletId);
        if (newOutlet == null) {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
//        newOutlet.getCars().size();
        Car carToUpdate = em.find(Car.class, car.getCarId());
        if (carToUpdate.getCarCategory().getCarCategoryId() != carCategoryId) {
            carToUpdate.getCarCategory().getCars().remove(carToUpdate);
            carToUpdate.setCarCategory(newCat);
            newCat.getCars().add(carToUpdate);
        }
        if (carToUpdate.getCarModel().getCarModelId() != modelId) {
            carToUpdate.getCarModel().getCars().remove(carToUpdate);
            carToUpdate.setCarModel(newModel);
            newModel.getCars().add(carToUpdate);
        }
        if (carToUpdate.getOutlet().getOutletId() != outletId) {
            carToUpdate.getOutlet().getCars().remove(carToUpdate);
            carToUpdate.setOutlet(newOutlet);
            newOutlet.getCars().add(car);
        }
        carToUpdate.setLicensePlate(car.getLicensePlate());
        carToUpdate.setColour(car.getColour());
        return carToUpdate;
    }

}
