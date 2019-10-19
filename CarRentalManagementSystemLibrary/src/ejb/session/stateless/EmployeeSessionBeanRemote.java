package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author darre
 */
@Remote
public interface EmployeeSessionBeanRemote {

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialsException;
    
}
