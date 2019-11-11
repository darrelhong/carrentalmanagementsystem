package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CarCategoryNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
@Stateless
@Local(RentalRateSessionBeanLocal.class)
@Remote(RentalRateSessionBeanRemote.class)
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public RentalRateSessionBean() {
    }

    // For remote client
    @Override
    public RentalRate createRentalRate(RentalRate newRentalRate, Long carCategoryId) throws UnknownPersistenceException, CarCategoryNotFoundException {
        CarCategory cat = em.find(CarCategory.class, carCategoryId);
        if (cat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + carCategoryId + " does not exist!");
        }
        try {
            em.persist(newRentalRate);
            cat.getRentalRates().add(newRentalRate);
            newRentalRate.setCarCategory(cat);
            em.flush();
            return newRentalRate;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new rental rate. " + ex.getMessage());
        }
    }

    // For local client
    @Override
    public RentalRate createRentalRate(RentalRate newRentalRate, CarCategory carCategory) throws UnknownPersistenceException {
        CarCategory cat = em.find(CarCategory.class, carCategory.getCarCategoryId());
        try {
            em.persist(newRentalRate);
            cat.getRentalRates().add(newRentalRate);
            newRentalRate.setCarCategory(cat);
            em.flush();
            return newRentalRate;
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Could not create new rental rate. " + ex.getMessage());
        }
    }

    @Override
    public List retreiveAllRentalRates() {
        Query q = em.createQuery("SELECT r from RentalRate r WHERE r.disabled = FALSE ORDER BY r.carCategory");
        return q.getResultList();
    }

    @Override
    public RentalRate retrieveRentalRateById(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rate = em.find(RentalRate.class, rentalRateId);
        if (rate == null) {
            throw new RentalRateNotFoundException("Rental rate with ID " + rentalRateId + " does not exist!");
        } else {
            return rate;
        }
    }

    @Override
    public RentalRate updateRate(RentalRate rentalRate, Long carCategoryId) throws CarCategoryNotFoundException {
        CarCategory newCat = em.find(CarCategory.class, carCategoryId);
        if (newCat == null) {
            throw new CarCategoryNotFoundException("Car category ID " + carCategoryId + " does not exist!");
        }
        RentalRate rateToUpdate = em.find(RentalRate.class, rentalRate.getRentalRateId());
        if (rateToUpdate.getCarCategory().getCarCategoryId() != carCategoryId) {
            rateToUpdate.getCarCategory().getRentalRates().remove(rateToUpdate);
            rateToUpdate.setCarCategory(newCat);
            newCat.getRentalRates().add(rateToUpdate);
        }
        rateToUpdate.setName(rentalRate.getName());
        rateToUpdate.setRate(rentalRate.getRate());
        rateToUpdate.setIsPromo(rentalRate.getIsPromo());
        rateToUpdate.setStartDate(rentalRate.getStartDate());
        rateToUpdate.setEndDate(rentalRate.getEndDate());
        return rateToUpdate;
    }

    @Override
    public Integer deleteRate(Long rateId) throws RentalRateNotFoundException {
        RentalRate toDelete = em.find(RentalRate.class, rateId);
        if (toDelete == null) {
            throw new RentalRateNotFoundException("Rental rate ID " + rateId + " does not exist!");
        }
        // Rental rate only deleted when carcategory deleted
        toDelete.setDisabled(true);
        return 1;
    }
}
