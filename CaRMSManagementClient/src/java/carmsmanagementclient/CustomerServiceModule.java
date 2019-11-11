package carmsmanagementclient;

import ejb.session.stateless.RentalRecordSessionBeanRemote;
import entity.Employee;
import entity.RentalRecord;
import java.util.Scanner;
import util.enumeration.EmployeeType;
import util.exception.CarNotAssignedException;
import util.exception.InvalidAccessRightsException;
import util.exception.RentalRecordNotFoundException;
import util.helper.Print;

/**
 *
 * @author darre
 */
public class CustomerServiceModule {

    private RentalRecordSessionBeanRemote rentalRecordSessionBeanRemote;

    private Employee currentEmployee;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(Employee currentEmployee, RentalRecordSessionBeanRemote rrsbr) {
        this.currentEmployee = currentEmployee;
        this.rentalRecordSessionBeanRemote = rrsbr;
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

        System.out.print("Enter booking ID> ");
        Long rentalRecordId = Long.parseLong(sc.nextLine().trim());

        try {
            RentalRecord result = rentalRecordSessionBeanRemote.pickupCar(rentalRecordId);
            System.out.println("Successful pickup!");
            Print.printRentalRecord(result);
        } catch (CarNotAssignedException | RentalRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doReturnCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer Service Menu :: Return Car ***\n");

        System.out.print("Enter booking ID> ");
        Long rentalRecordId = Long.parseLong(sc.nextLine().trim());
        
        try {
            RentalRecord result = rentalRecordSessionBeanRemote.returnCar(rentalRecordId);
            System.out.println("Successful return!");
            Print.printRentalRecord(result);
        } catch (RentalRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }  

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
}
