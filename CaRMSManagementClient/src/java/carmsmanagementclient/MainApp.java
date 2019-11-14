package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.RentalRecordSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidAccessRightsException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author
 */
public class MainApp {

    private CarSessionBeanRemote carSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private RentalRecordSessionBeanRemote rentalRecordSessionBeanRemote;

    private OperationsModule operationsModule;
    private CustomerServiceModule customerServiceModule;
    private SalesModule salesModule;

    private Employee currentEmployee;

    public MainApp() {
    }

    public MainApp(EmployeeSessionBeanRemote esbr, CarModelSessionBeanRemote cmsbr,
            CarSessionBeanRemote csbr, CarCategorySessionBeanRemote ccsbr,
            OutletSessionBeanRemote osbr, RentalRateSessionBeanRemote rrsbr,
            RentalRecordSessionBeanRemote rrsbr1) {
        this.employeeSessionBeanRemote = esbr;
        this.carModelSessionBeanRemote = cmsbr;
        this.carSessionBeanRemote = csbr;
        this.carCategorySessionBeanRemote = ccsbr;
        this.outletSessionBeanRemote = osbr;
        this.rentalRateSessionBeanRemote = rrsbr;
        this.rentalRecordSessionBeanRemote = rrsbr1;
    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Management Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login success!\n");

                        operationsModule = new OperationsModule(currentEmployee,
                                carModelSessionBeanRemote, carSessionBeanRemote,
                                carCategorySessionBeanRemote, outletSessionBeanRemote, rentalRecordSessionBeanRemote,
                                employeeSessionBeanRemote);
                        customerServiceModule = new CustomerServiceModule(currentEmployee, rentalRecordSessionBeanRemote);
                        salesModule = new SalesModule(currentEmployee, rentalRateSessionBeanRemote, carCategorySessionBeanRemote);
                        menuMain();
                    } catch (InvalidLoginCredentialsException ex) {
                        System.out.println("Invalid login credentials " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialsException {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMS Management Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialsException("\nMissing login creditials!");
        }
    }

    private void menuMain() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Management Client ***\n");
            System.out.println("You are logged in as " + currentEmployee.getName()
                    + " with " + currentEmployee.getEmployeeType() + " rights.\n");
            System.out.println("1: Sales Menu");
            System.out.println("2: Operations Menu");
            System.out.println("3: Customer Service Menu");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        salesModule.menuSales();
                    } catch (InvalidAccessRightsException ex) {
                        System.out.println("You do not have the rights to access this menu. "
                                + ex.getMessage() + " Please try again.\n");
                    }
                } else if (response == 2) {
                    try {
                        operationsModule.menuOperations();
                    } catch (InvalidAccessRightsException ex) {
                        System.out.println("You do not have the rights to access this menu. "
                                + ex.getMessage() + " Please try again.\n");
                    }
                } else if (response == 3) {
                    try {
                        customerServiceModule.menuCustomerService();
                    } catch (InvalidAccessRightsException ex) {
                        System.out.println("You do not have the rights to access this menu. "
                                + ex.getMessage() + " Please try again.\n");
                    }
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
}
