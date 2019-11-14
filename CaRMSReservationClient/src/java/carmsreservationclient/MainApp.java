package carmsreservationclient;

import ejb.session.stateful.BookingSessionBeanRemote;
import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRecordSessionBeanRemote;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Outlet;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CancellationErrorException;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InsufficientInventoryException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.NoRateFoundException;
import util.exception.OutletClosedException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRecordNotFoundException;
import util.exception.UnknownPersistenceException;
import util.helper.Print;

/**
 *
 * @author darre
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private BookingSessionBeanRemote bookingSessionBeanRemote;
    private RentalRecordSessionBeanRemote rentalRecordSessionBeanRemote;

    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote csbr, CarCategorySessionBeanRemote ccsbr,
            CarModelSessionBeanRemote cmsbr, CarSessionBeanRemote csbr1,
            OutletSessionBeanRemote osbr, BookingSessionBeanRemote bsbr, RentalRecordSessionBeanRemote rrsbr) {
        this.customerSessionBeanRemote = csbr;
        this.carCategorySessionBeanRemote = ccsbr;
        this.carModelSessionBeanRemote = cmsbr;
        this.carSessionBeanRemote = csbr1;
        this.outletSessionBeanRemote = osbr;
        this.bookingSessionBeanRemote = bsbr;
        this.rentalRecordSessionBeanRemote = rrsbr;
    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Reservation Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register as Customer");
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    if (currentCustomer == null) {
                        try {
                            doLogin();

                            menuCustomer();
                        } catch (InvalidLoginCredentialsException ex) {
                            System.out.println("Invalid login credentials " + ex.getMessage() + "\n");
                        }
                    } else {
                        System.out.println("You are already logged in");
                    }
                } else if (response == 2) {
                    doRegisterCustomer();
                } else if (response == 3) {
                    doSearchCar();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again.\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void doRegisterCustomer() {
        Scanner sc = new Scanner(System.in);
        Customer customer = new Customer();

        System.out.println("*** CaRMS Reservation Client :: Register as Customer ***\n");
        System.out.print("Enter name> ");
        customer.setName(returnNonEmptyString(sc));
        System.out.print("Enter email> ");
        customer.setEmail(returnNonEmptyString(sc));
        System.out.print("Enter password> ");
        customer.setPassword(returnNonEmptyString(sc));

        try {
            customer = customerSessionBeanRemote.createNewCustomer(customer);
            System.out.println("New customer created. ID = " + customer.getCustomerId()
                    + " username: " + customer.getName() + " email: " + customer.getEmail());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doLogin() throws InvalidLoginCredentialsException {
        Scanner sc = new Scanner(System.in);
        String email = "";
        String password = "";

        System.out.println("*** CaRMS Reservation Client :: Login ***\n");
        System.out.print("Enter email> ");
        email = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (email.length() > 0 && password.length() > 0) {
            currentCustomer = customerSessionBeanRemote.customerLogin(email, password);
        } else {
            throw new InvalidLoginCredentialsException("Missing login credentials!");
        }
    }

    private void doSearchCar() {
        Scanner sc = new Scanner(System.in);
        String response = "";
        Long categoryId;
        Long modelId;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date start;
        Date end;
        Long pickupOutletId;
        Long returnOutletId;

        System.out.println("*** CaRMS Reservation Client :: Search Car ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories:");
        Print.printListCarCategories(categories);

        while (true) {
            System.out.print("Enter desired car category> ");
            response = sc.nextLine().trim();
            if (Long.parseLong(response) < 0) {
                System.out.println("Invalid response, Please try again");
            } else {
                categoryId = Long.parseLong(response);
                break;
            }
        }
        List<CarModel> carModels = carModelSessionBeanRemote.retrieveCarModelsByCategory(categoryId);
        if (carModels.isEmpty()) {
            System.out.println("No models available");
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
            return;
        }
        Print.printListCarModels(carModels);
        while (true) {
            System.out.print("Enter desired car model (Enter 0 for any)> ");
            response = sc.nextLine().trim();
            if (Long.parseLong(response) < 0) {
                System.out.println("Invalid response, please try again");
            } else {
                modelId = Long.parseLong(response);
                break;
            }
        }
        while (true) {
            try {
                System.out.print("Enter pickup date and time (dd/mm/yyyy hh:mm AM/PM)> ");
                start = inputDateFormat.parse(sc.nextLine().trim());
                System.out.print("Enter return date and time (dd/mm/yyyy hh:mm AM/PM)> ");
                end = inputDateFormat.parse(sc.nextLine().trim());
                break;
            } catch (ParseException ex) {
                System.out.println("Invalid date time input, please try again");
            }
        }
        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        Print.printListOutlets(outlets);
        System.out.print("\nEnter pickup outlet ID> ");
        pickupOutletId = Long.parseLong(sc.nextLine().trim());
        System.out.print("Enter return outlet ID> ");
        returnOutletId = Long.parseLong(sc.nextLine().trim());
        if (modelId != 0) {
            try {
                BigDecimal finalRate = bookingSessionBeanRemote.searchByCarModel(categoryId, modelId, start, end, pickupOutletId, returnOutletId);
                System.out.print("Car model available! Total rate: S$" + finalRate + ". Confirm booking? (Press 'Y' to confirm)> ");
                response = sc.nextLine().trim();
                if (response.toLowerCase().equals("y")) {
                    if (currentCustomer != null) {
                        doReserveCar();
                    } else {
                        System.out.println("Please log in to make reservation.");
                    }
                } else {
                    System.out.println("Reservation cancelled");
                }
            } catch (CarCategoryNotFoundException | CarModelNotFoundException | OutletNotFoundException
                    | NoRateFoundException | InsufficientInventoryException | OutletClosedException ex) {
                System.out.println("\n" + ex.getMessage());
            }
        } else {
            try {
                BigDecimal finalRate = bookingSessionBeanRemote.searchByCarCategory(categoryId, start, end, pickupOutletId, returnOutletId);
                System.out.print("Car available! Total rate: S$" + finalRate + ". Confirm booking? (Press 'Y' to confirm)> ");
                response = sc.nextLine().trim();
                if (response.toLowerCase().equals("y")) {
                    if (currentCustomer != null) {
                        doReserveCar();
                    } else {
                        System.out.println("Please log in to make reservation.");
                    }
                } else {
                    System.out.println("Reservation cancelled");
                }
            } catch (CarCategoryNotFoundException | OutletNotFoundException | NoRateFoundException | InsufficientInventoryException | OutletClosedException ex) {
                System.out.println("\n" + ex.getMessage());
            }
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doReserveCar() {
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.print("Enter credit card number> ");
        String ccNum = sc.nextLine().trim();
        System.out.println("Select option: 1. Immediate payment | 2. Deferred payment");
        while (true) {
            input = sc.nextLine().trim();
            if (input.equals("1")) {
                try {
                    bookingSessionBeanRemote.confirmReservation(currentCustomer, ccNum, true);
                    System.out.println("Reservation confirmed!");
                } catch (CustomerNotFoundException | UnknownPersistenceException ex) {
                    System.out.println("\n" + ex.getMessage());
                }
                break;
            } else if (input.equals("2")) {
                try {
                    bookingSessionBeanRemote.confirmReservation(currentCustomer, ccNum, false);
                    System.out.println("Reservation confirmed!");
                } catch (CustomerNotFoundException | UnknownPersistenceException ex) {
                    System.out.println("\n" + ex.getMessage());
                }
                break;
            } else {
                System.out.println("Invalid input, please try again.");
            }
        }
    }

    private void menuCustomer() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** CaRMS Reservation Client :: Customer Menu ***\n");
            System.out.println("You are logged in as " + currentCustomer.getName() + ".\n");
            System.out.println("1: Search Car");
            System.out.println("2: View All Reservations");
            System.out.println("3: View Reservation Details");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    doViewAllReservations();
                } else if (response == 3) {
                    doViewReservationDetails();
                } else if (response == 4) {
                    currentCustomer = null;
                    break;
                } else {
                    System.out.println("Invalid option, please try again.\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void doViewAllReservations() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: View All Reservations ***\n");
        try {
            List<RentalRecord> records = rentalRecordSessionBeanRemote.retrieveAllCustomerReservations(currentCustomer);
            Print.printListRentalRecords(records);
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doViewReservationDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: View Reservation Details ***\n");

        System.out.print("Enter Booking ID> ");
        Long bookingId = sc.nextLong();
        sc.nextLine();

        try {
            RentalRecord record = rentalRecordSessionBeanRemote.retrieveRentalRecordById(bookingId);
            Print.printRentalRecord(record);

            doCancelReservation(record);
        } catch (RentalRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doCancelReservation(RentalRecord record) {
        Scanner sc = new Scanner(System.in);
        String response = "";
        System.out.println("\n1. Cancel Reservation");
        System.out.println("2. Back\n");

        System.out.print("> ");
        response = sc.nextLine().trim();

        if (response.equals("1")) {
            if (record.getCancelled()) {
                System.out.println("Booking already cancelled");
            } else {
                try {
                    System.out.println(rentalRecordSessionBeanRemote.cancelReservation(record.getRentalRecordId()));
                } catch (CancellationErrorException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    private String returnNonEmptyString(Scanner sc) {
        String result = "";
        while (true) {
            result = sc.nextLine().trim();
            if (result.length() > 0) {
                break;
            } else {
                System.out.println("Non empty string required!");
            }
        }
        return result;
    }
}
