package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialsException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 
 */
@Stateless
@Local(PartnerSessionBeanLocal.class)
@Remote(PartnerSessionBeanRemote.class)
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public PartnerSessionBean() {
    }

    @Override
    public Partner createNewPartner(Partner newPartner) throws UnknownPersistenceException {
        try {
            em.persist(newPartner);
            em.flush();
            return newPartner;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialsException {
        try {
            Partner partner = retrievePartnerByUsername(username);
            if (partner.getPassword().equals(password)) {
                return partner;
            } else {
                throw new InvalidLoginCredentialsException("Wrong password");
            }
        } catch (PartnerNotFoundException ex) {
            throw new InvalidLoginCredentialsException("Invalid user");
        }
    }

    @Override
    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Partner) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner with username " + username + " does not exist!");
        }
    }

}
