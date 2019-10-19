package ejb.session.stateless;

import entity.CarCategory;
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

}