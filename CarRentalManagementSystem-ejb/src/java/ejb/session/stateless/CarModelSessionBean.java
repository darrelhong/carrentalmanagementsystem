package ejb.session.stateless;

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
@Local(CarModelSessionBeanLocal.class)
@Remote(CarModelSessionBeanRemote.class)
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public CarModelSessionBean() {
    }

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
}
