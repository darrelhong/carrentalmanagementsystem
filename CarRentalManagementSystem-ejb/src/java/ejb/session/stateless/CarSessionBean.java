package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
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

    @Override
    public Car createNewCar(Car newCar, CarCategory carCategory, CarModel carModel) throws UnknownPersistenceException {
        CarCategory cat = em.find(CarCategory.class, carCategory.getCarCategoryId());
        CarModel model = em.find(CarModel.class, carModel.getCarModelId());
        try {
            em.persist(newCar);
            newCar.setCarCategory(cat);
            cat.getCars().add(newCar);
            newCar.setCarModel(model);
            model.getCars().add(newCar);
            em.flush();
            return newCar;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new car " + ex.getMessage());
        }        
    }
}
