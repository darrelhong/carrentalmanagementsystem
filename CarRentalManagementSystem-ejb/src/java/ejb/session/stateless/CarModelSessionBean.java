package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
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
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
@Stateless
@Local(CarModelSessionBeanLocal.class)
@Remote(CarModelSessionBeanRemote.class)
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public CarModelSessionBean() {
    }
    // For local client
    @Override
    public CarModel createNewCarModel(CarModel newCarModel, CarCategory carCategory) throws UnknownPersistenceException {
        CarCategory cat = em.find(CarCategory.class, carCategory.getCarCategoryId());
        try {
            em.persist(newCarModel);
            newCarModel.setCarCategory(cat);
            cat.getCarModels().add(newCarModel);
            em.flush();
            return newCarModel;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create card model " + ex.getMessage());
        }
    }
    
    // For remote client
    @Override
    public CarModel createNewCarModel(CarModel newCarModel, Long categoryId) throws UnknownPersistenceException, CarCategoryNotFoundException {
        CarCategory cat = em.find(CarCategory.class, categoryId);
        if (cat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + categoryId + " does not exist!");
        }
        try {
            em.persist(newCarModel);
            newCarModel.setCarCategory(cat);
            cat.getCarModels().add(newCarModel);
            em.flush();
            return newCarModel;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create card model " + ex.getMessage());
        }
    }

    @Override
    public List retrieveAllCarModels() {
        Query query = em.createQuery("SELECT m FROM CarModel m WHERE m.disabled = FALSE ORDER BY m.carCategory, m.make, m.model");
        return query.getResultList();
    }

    @Override
    public List retrieveCarModelsByCategory(Long categoryId) {
        Query query = em.createQuery("SELECT m FROM CarModel m WHERE m.disabled = FALSE AND m.carCategory.carCategoryId = :inCategoryId ORDER BY m.make, m.model");
        query.setParameter("inCategoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public CarModel updateCarModel(CarModel updatedCarModel, Long modelId, Long categoryId) throws CarCategoryNotFoundException, CarModelNotFoundException {
        CarCategory newCat = em.find(CarCategory.class, categoryId);
        if (newCat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + categoryId + " does not exist!");
        }
        CarModel carModelToUpdate = em.find(CarModel.class, modelId);
        if (carModelToUpdate == null) {
            throw new CarModelNotFoundException("Car model ID " + modelId + " does not exist!");
        }
        if (carModelToUpdate.getCarCategory().getCarCategoryId() != categoryId) {
            carModelToUpdate.getCarCategory().getCarModels().remove(carModelToUpdate);
            carModelToUpdate.setCarCategory(newCat);
            newCat.getCarModels().add(carModelToUpdate);
        }
        carModelToUpdate.setMake(updatedCarModel.getMake());
        carModelToUpdate.setModel(updatedCarModel.getModel());
        return carModelToUpdate;
    }

    @Override
    public Integer deleteCarModel(Long modelId) throws CarModelNotFoundException {
        CarModel toDelete = em.find(CarModel.class, modelId);
        if (toDelete == null) {
            throw new CarModelNotFoundException("Car model ID " + modelId + " does not exist!");
        }
        toDelete.getCarCategory().getCarModels().remove(toDelete);
        if (toDelete.getCars().isEmpty()) {
            em.remove(toDelete);
            return 0;
        } else {
            toDelete.setDisabled(true);
            return 1;
        }
    }

    @Override
    public CarModel retrieveCarModelById(Long modelId) throws CarModelNotFoundException {
        CarModel carModel = em.find(CarModel.class, modelId);
        if (carModel == null) {
            throw new CarModelNotFoundException("Car model ID " + modelId + " does not exist!");
        }
        return carModel;
    }
    
}
