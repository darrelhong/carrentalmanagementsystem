package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
@Stateless
@Local(CustomerSessionBeanLocal.class)
@Remote(CustomerSessionBeanRemote.class)
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public CustomerSessionBean() {
    }

    @Override
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (Customer) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer with email " + email + " does not exist!");
        }
    }

    @Override
    public Customer customerLogin(String email, String password) throws InvalidLoginCredentialsException {
        try {
            Customer customer = retrieveCustomerByEmail(email);
            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialsException("Wrong password");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialsException("Invalid user");
        }
    }

    @Override
    public Customer createNewCustomer(Customer newCustomer) throws UnknownPersistenceException {
        try {
            em.persist(newCustomer);
            em.flush();
            return newCustomer;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new customer. " + ex.getMessage());
        }
    }
    
    
}
