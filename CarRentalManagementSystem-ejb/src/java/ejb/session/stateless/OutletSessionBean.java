package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
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
@Local(OutletSessionBeanLocal.class)
@Remote(OutletSessionBeanRemote.class)
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public OutletSessionBean() {
    }

    @Override
    public Outlet createNewOutletWithEmployees(Outlet newOutlet) throws UnknownPersistenceException {
        try {
            em.persist(newOutlet);
            for (Employee e : newOutlet.getEmployees()) {
                e.setOutlet(newOutlet);
                em.persist(e);
            }
            em.flush();
            return newOutlet;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new outlet with employess " + ex.getMessage());
        }
    }

}
