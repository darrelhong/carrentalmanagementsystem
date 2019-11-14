package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 
 */
@Stateless
@Local(CarCategorySessionBeanLocal.class)
@Remote(CarCategorySessionBeanRemote.class)
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public CarCategorySessionBean() {
    }

    @Override
    public CarCategory createNewCarCategory(CarCategory newCarCategory) throws UnknownPersistenceException {
        try {
            em.persist(newCarCategory);
            em.flush();
            return newCarCategory;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new car category " + ex.getMessage());
        }
    }

    @Override
    public List retrieveAllCarCategories() {
        Query query = em.createQuery("SELECT c from CarCategory c ORDER BY c.carCategoryId");
        return query.getResultList();
    }

    @Override
    public CarCategory retrieveCarCategoryById(Long categoryId) throws CarCategoryNotFoundException {
        CarCategory result = em.find(CarCategory.class, categoryId);
        if (result != null) {
            return result;
        } else {
            throw new CarCategoryNotFoundException("Car category with ID: " + categoryId + " does not exist!");
        }
    }
}
