package ejb.session.stateless;

import entity.Employee;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialsException;

public interface EmployeeSessionBeanRemote {

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialsException;
    
}
