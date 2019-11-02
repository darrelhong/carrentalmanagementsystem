package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

public interface OutletSessionBeanLocal {

    Outlet createNewOutletWithEmployees(Outlet newOutlet) throws UnknownPersistenceException;

    List retrieveAllOutlets();

    Outlet retrieveOutletByOutletId(Long outletId) throws OutletNotFoundException;
    
}
