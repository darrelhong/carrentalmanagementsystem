package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.OutletNotFoundException;
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

    @Override
    public List retrieveAllOutlets() {
        Query query = em.createQuery("SELECT o from Outlet o ORDER BY o.outletId");
        return query.getResultList();
    }

    @Override
    public Outlet retrieveOutletByOutletId(Long outletId) throws OutletNotFoundException {
        Outlet outlet = em.find(Outlet.class, outletId);
        if (outlet != null) {
            return outlet;
        } else {
            throw new OutletNotFoundException("Outlet with ID " + outletId + " does not exist!");
        }
    }
    
}
