package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialsException;

public interface EmployeeSessionBeanLocal {

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialsException;

    List retrieveAllEmployeesByOutlet(java.lang.Long outletId);

    Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
    
}
