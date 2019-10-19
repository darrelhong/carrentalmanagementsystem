package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author darre
 */
@Local
public interface EmployeeSessionBeanLocal {

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialsException;
    
}
