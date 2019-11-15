package ejb.session.stateless;

import entity.Partner;
import util.exception.InvalidLoginCredentialsException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

public interface PartnerSessionBeanLocal {

    Partner createNewPartner(Partner newPartner) throws UnknownPersistenceException;

    Partner partnerLogin(String username, String password) throws InvalidLoginCredentialsException;

    Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException;
    
}
