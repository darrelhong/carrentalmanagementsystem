package carmsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
public class MainApp {

    private CustomerSessionBeanRemote customerSessionBeanRemote;

    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerSessionBeanRemote csbr) {
        this.customerSessionBeanRemote = csbr;
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
        System.out.println("*** CaRMS Reservation Client :: Search Car ***\n");
        System.out.println("Unsupported action");
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
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
        System.out.println("Unsupported action");
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doViewReservationDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: View Reservation Details ***\n");
        System.out.println("Unsupported action");
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
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
