package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.RentalRecordSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author darre
 */
public class Main {

    @EJB(name = "RentalRecordSessionBeanRemote")
    private static RentalRecordSessionBeanRemote rentalRecordSessionBeanRemote;

    @EJB(name = "RentalRateSessionBeanRemote")
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    @EJB(name = "OutletSessionBeanRemote")
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    @EJB(name = "CarCategorySessionBeanRemote")
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    @EJB(name = "CarSessionBeanRemote")
    private static CarSessionBeanRemote carSessionBeanRemote;

    @EJB(name = "CarModelSessionBeanRemote")
    private static CarModelSessionBeanRemote carModelSessionBeanRemote;

    @EJB(name = "EmployeeSessionBeanRemote")
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, carModelSessionBeanRemote, 
                carSessionBeanRemote, carCategorySessionBeanRemote, outletSessionBeanRemote, rentalRateSessionBeanRemote,
        rentalRecordSessionBeanRemote);
        mainApp.runApp();
        System.out.println("thdtr");
    }
    
}
