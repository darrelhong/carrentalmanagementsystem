package ejb.session.stateless;

import entity.Customer;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

public interface CustomerSessionBeanLocal {

    Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    Customer customerLogin(String email, String password) throws InvalidLoginCredentialsException;

    Customer createNewCustomer(Customer newCustomer) throws UnknownPersistenceException;
    
}
