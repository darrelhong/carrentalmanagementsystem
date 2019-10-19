package ejb.session.stateless;

import entity.Outlet;
import util.exception.UnknownPersistenceException;

public interface OutletSessionBeanLocal {

    Outlet createNewOutletWithEmployees(Outlet newOutlet) throws UnknownPersistenceException;
    
}
