package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import util.exception.UnknownPersistenceException;


public interface OutletSessionBeanRemote {

    Outlet createNewOutletWithEmployees(Outlet newOutlet) throws UnknownPersistenceException;

    List retrieveAllOutlets();
    
}
