package carmsmanagementclient;

import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeType;
import util.exception.InvalidAccessRightsException;

/**
 *
 * @author darre
 */
public class CustomerServiceModule {

    private Employee currentEmployee;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public void menuCustomerService() throws InvalidAccessRightsException {
        if (currentEmployee.getEmployeeType() == EmployeeType.ADMIN
                || currentEmployee.getEmployeeType() == EmployeeType.CUSTOMERSERVICE) {

            Scanner sc = new Scanner(System.in);
            Integer response = 0;

            while (true) {
                System.out.println("*** CaRMS Management Client :: Customer Service Menu ***\n");
                System.out.println("1: Pickup Car");
                System.out.println("2: Return Car");
                System.out.println("3: Back\n");
                response = 0;

                while (response < 1 || response > 3) {
                    System.out.print("> ");
                    response = sc.nextInt();
                    if (response == 1) {
                        doPickupCar();
                    } else if (response == 2) {
                        doReturnCar();
                    } else if (response == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }
                if (response == 3) {
                    break;
                }
            }
        } else {
            throw new InvalidAccessRightsException("Operations menu requires ADMIN or OPERATIONS rights.");
        }
    }

    private void doPickupCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer Service Menu :: Pickup Car ***\n");
        System.out.println("Unsupported action");
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doReturnCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer Service Menu :: Return Car ***\n");
        System.out.println("Unsupported action");
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
}
