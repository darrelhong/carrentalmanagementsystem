package ejb.session.stateless;

import entity.Customer;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerNotFoundException;
import util.exception.RentalRecordNotFoundException;

/**
 *
 * @author darre
 */
@Stateless
@Local(RentalRecordSessionBeanLocal.class)
@Remote(RentalRecordSessionBeanRemote.class)
public class RentalRecordSessionBean implements RentalRecordSessionBeanRemote, RentalRecordSessionBeanLocal {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "CarRentalManagementSystem-ejbPU")
    private EntityManager em;

    public RentalRecordSessionBean() {
    }

    @Override
    public List retrieveAllCustomerReservations(Customer customer) throws CustomerNotFoundException {
        try {
            Customer cust = customerSessionBeanLocal.retrieveCustomerByCustomerId(customer.getCustomerId());
            cust.getRentalRecords().size();
            return cust.getRentalRecords();
        } catch (CustomerNotFoundException ex) {
            throw ex;
        }
    }

    @Override
    public RentalRecord retrieveRentalRecordById(Long bookingId) throws RentalRecordNotFoundException {
        RentalRecord record = em.find(RentalRecord.class, bookingId);
        if (record != null) {
            return record;
        } else {
            throw new RentalRecordNotFoundException("Booking with ID " + bookingId + " not found!");
        }
    }

    @Override
    public String cancelReservation(RentalRecord record) {
        RentalRecord toCancel = em.find(RentalRecord.class, record.getRentalRecordId());

        BigDecimal penalty = calculatePenalty(toCancel);
        toCancel.setPenaltyAmount(penalty);
        toCancel.setCancelled(true);
        if (record.getPaid()) {
            BigDecimal refundAmount = toCancel.getAmount().subtract(penalty);
            System.out.println(refundAmount);
            toCancel.setAmountToRefund(refundAmount);
            return String.format("Reservation cancelled. Amount to refund: %8.2f", refundAmount);
        } else {
            return String.format("Reservation cancelled. Penalty charged: %8.2f", penalty);
        }
    }

    private BigDecimal calculatePenalty(RentalRecord toCancel) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
            Date now = sdf.parse("1/1/2019 12:00 AM");
            BigDecimal result = new BigDecimal(0);
            long timeDiff = toCancel.getStartDateTime().getTime() - now.getTime();
            if (timeDiff > 14 * 24 * 3600 * 1000) {
                System.out.println("No penalty");
                System.out.println("penalty amount " + result);
                return result;
            }
            if (timeDiff > 7 * 24 * 3600 * 1000) {
                System.out.println("0.2 penalty");
                result = toCancel.getAmount().multiply(new BigDecimal(0.2));
                System.out.println("penalty amount " + result);
                return result;
            }
            if (timeDiff > 3 * 24 * 3600 * 1000) {
                result = toCancel.getAmount().multiply(new BigDecimal(0.5));
                System.out.println("0.5 penalty");
                System.out.println("penalty amount " + result);
                return result;
            }
            result = toCancel.getAmount().multiply(new BigDecimal(0.7));
            System.out.println("0.7 penalty");
            System.out.println("penalty amount " + result);
            return result;
        } catch (ParseException ex) {
            return null;
        }
    }

}
