package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelDisabledException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.EntityDisabledException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

@Stateless
@Local(CarSessionBeanLocal.class)
@Remote(CarSessionBeanRemote.class)
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CarSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // For data initialisation
    @Override
    public Car createNewCar(Car newCar, CarCategory carCategory, CarModel carModel, Outlet outlet) throws UnknownPersistenceException, InputDataValidationException {
        CarCategory cat = em.find(CarCategory.class, carCategory.getCarCategoryId());
        CarModel model = em.find(CarModel.class, carModel.getCarModelId());
        Outlet out = em.find(Outlet.class, outlet.getOutletId());
        try {
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);
            if (constraintViolations.isEmpty()) {
                em.persist(newCar);
                newCar.setCarCategory(cat);
                cat.getCars().add(newCar);
                newCar.setCarModel(model);
                model.getCars().add(newCar);
                newCar.setOutlet(out);
                out.getCars().add(newCar);
                em.flush();
                return newCar;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new car " + ex.getMessage());
        }
    }
    
    // For remote client
    @Override
    public Car createNewCar(Car newCar, Long categoryId, Long modelId, Long outletId) throws UnknownPersistenceException, EntityDisabledException, CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, CarModelDisabledException, InputDataValidationException {
        CarCategory cat = em.find(CarCategory.class, categoryId);
        if (cat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + categoryId + " does not exist!");
        }
        CarModel model = em.find(CarModel.class, modelId);
        if (model == null) {
            throw new CarModelNotFoundException("Car mode ID " + modelId + " does not exist!");
        }
        if (model.getDisabled()) {
            throw new CarModelDisabledException("Cannot create car. Car model " + model.getMake() + " " + model.getModel() + " is disabled!");
        }
        Outlet outlet = em.find(Outlet.class, outletId);
        if (outlet == null) {
            throw new OutletNotFoundException("Outlet ID " + outletId + " does not exist!");
        }
        if (model.getDisabled()) {
            throw new EntityDisabledException("Car model ID " + modelId + " is disabled!");
        }
        try {
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);
            if (constraintViolations.isEmpty()) {
                em.persist(newCar);
                newCar.setCarCategory(cat);
                cat.getCars().add(newCar);
                newCar.setCarModel(model);
                model.getCars().add(newCar);
                newCar.setOutlet(outlet);
                outlet.getCars().add(newCar);
                em.flush();
                return newCar;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
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
        if (toDelete.getAvailabilityStatus() && toDelete.getRentalRecords().isEmpty()) {
            em.remove(toDelete);
            return 0;
        } else {
            toDelete.setAvailabilityStatus(false);
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
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
}
