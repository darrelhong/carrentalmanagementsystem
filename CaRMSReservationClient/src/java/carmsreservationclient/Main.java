package carmsreservationclient;

import ejb.session.stateful.BookingSessionBeanRemote;
import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRecordSessionBeanRemote;
import javax.ejb.EJB;

public class Main {

    @EJB(name = "RentalRecordSessionBeanRemote")
    private static RentalRecordSessionBeanRemote rentalRecordSessionBeanRemote;

    @EJB(name = "BookingSessionBeanRemote")
    private static BookingSessionBeanRemote bookingSessionBeanRemote;

    @EJB(name = "OutletSessionBeanRemote")
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    @EJB(name = "CarSessionBeanRemote")
    private static CarSessionBeanRemote carSessionBeanRemote;

    @EJB(name = "CarModelSessionBeanRemote")
    private static CarModelSessionBeanRemote carModelSessionBeanRemote;

    @EJB(name = "CarCategorySessionBeanRemote")
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    @EJB(name = "CustomerSessionBeanRemote")
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerSessionBeanRemote, carCategorySessionBeanRemote,
                carModelSessionBeanRemote, carSessionBeanRemote, outletSessionBeanRemote,
                bookingSessionBeanRemote, rentalRecordSessionBeanRemote);
        mainApp.runApp();
    }

}
