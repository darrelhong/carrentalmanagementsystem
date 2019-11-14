package ejb.session.stateless;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author 
 */
@Stateless
public class AllocationTimerSessionBean implements AllocationSessionBeanRemote, AllocationSessionBeanLocal {

    @EJB(name = "RentalRecordSessionBeanLocal")
    private RentalRecordSessionBeanLocal rentalRecordSessionBeanLocal;

    @Schedule(dayOfWeek = "*", hour = "2")
    public void allocateCarsTimer() {
        System.out.println("Allocate cars event: " + new Date());
        
        rentalRecordSessionBeanLocal.allocateCars(new Date());
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
