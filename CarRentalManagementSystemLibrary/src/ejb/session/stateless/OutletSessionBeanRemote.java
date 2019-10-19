package ejb.session.stateless;

import entity.Outlet;
import util.exception.UnknownPersistenceException;


public interface OutletSessionBeanRemote {

    Outlet createNewOutletWithEmployees(Outlet newOutlet) throws UnknownPersistenceException;
    
}
