package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.CarCategory;
import entity.Employee;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeType;
import util.exception.CarCategoryNotFoundException;
import util.exception.InvalidAccessRightsException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
public class SalesModule {

    private Employee currentEmployee;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    public SalesModule() {
    }

    public SalesModule(Employee currentEmployee, RentalRateSessionBeanRemote rrsbr,
            CarCategorySessionBeanRemote ccsbr) {
        this.currentEmployee = currentEmployee;
        this.rentalRateSessionBeanRemote = rrsbr;
        this.carCategorySessionBeanRemote = ccsbr;
    }

    public void menuSales() throws InvalidAccessRightsException {
        if (currentEmployee.getEmployeeType() == EmployeeType.ADMIN
                || currentEmployee.getEmployeeType() == EmployeeType.SALES) {

            Scanner sc = new Scanner(System.in);
            Integer response = 0;

            while (true) {
                System.out.println("*** CaRMS Management Client :: Sales Management Menu ***\n");
                System.out.println("1: Create Rental Rate");
                System.out.println("2: View All Rental Rates");
                System.out.println("3: View Rental Rate Details");
                System.out.println("4: Back\n");
                response = 0;

                while (response < 1 || response > 4) {
                    System.out.print("> ");
                    response = sc.nextInt();
                    if (response == 1) {
                        createRentalRate();
                    } else if (response == 2) {
                        viewAllRentalRates();
                    } else if (response == 3) {
                        viewRentalRateDetails();
                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }
                if (response == 4) {
                    break;
                }
            }
        }
    }

    private void createRentalRate() {
        Scanner sc = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        System.out.println("*** Sales Management Menu :: Create Rental Rate ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories:");
        System.out.printf("%11s%20s\n", "Category ID", "Category Name");
        categories.forEach((cat) -> {
            System.out.printf("%11s%20s\n", cat.getCarCategoryId().toString(), cat.getCarCategory());
        });
        System.out.print("Enter new rental rate car category ID> ");
        Long categoryId = Long.parseLong(sc.nextLine().trim());
        System.out.print("Enter name of rate> ");
        newRentalRate.setName(returnNonEmptyString(sc));
        System.out.print("Enter rate amount> ");
        newRentalRate.setRate(new BigDecimal(sc.nextLine().trim()));
        Integer isPromo;
        while (true) {
            System.out.print("Enter rate type (0: Normal, 1: Promo)> ");
            isPromo = sc.nextInt();
            if (isPromo == 0) {
                newRentalRate.setIsPromo(false);
                break;
            } else if (isPromo == 1) {
                newRentalRate.setIsPromo(true);
                break;
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
        sc.nextLine();
        Integer day;
        while (true) {
            System.out.print("Enter valid days (1: Monday ... 7: Sunday, -1 to complete)> ");
            day = sc.nextInt();
            if (day == -1) {
                break;
            } else {
                newRentalRate.getValidityPeriod().add(day);
                System.out.println(newRentalRate.getValidityPeriod());
            }
        }
        sc.nextLine();
        try {
            newRentalRate = rentalRateSessionBeanRemote.createRentalRate(newRentalRate, categoryId);
            System.out.println("\nNew rate created!");
            System.out.printf("%2s%20s%40s%9s%18s\n", "ID", "Category", "Name", "Rate", "Valid Days");
            System.out.printf("%2s%20s%40s%9.2f%18s\n", newRentalRate.getRentalRateId(), newRentalRate.getCarCategory().getCarCategory(), newRentalRate.getName(), newRentalRate.getRate(), newRentalRate.getValidityPeriod());
        } catch (CarCategoryNotFoundException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void viewAllRentalRates() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Sales Management Menu :: View All Rental Rates ***\n");

        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retreiveAllRentalRates();
        System.out.printf("%2s%20s%40s%9s%18s\n", "ID", "Category", "Name", "Rate", "Valid Days");
        rentalRates.forEach((r) -> {
            System.out.printf("%2s%20s%40s%9.2f%18s\n", r.getRentalRateId(), r.getCarCategory().getCarCategory(), r.getName(), r.getRate(), r.getValidityPeriod());
        });

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void viewRentalRateDetails() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        System.out.println("*** Sales Management Menu :: View Rental Rate Details ***");

        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retreiveAllRentalRates();
        System.out.printf("%2s%20s%40s%9s%18s\n", "ID", "Category", "Name", "Rate", "Valid Days");
        rentalRates.forEach((r) -> {
            System.out.printf("%2s%20s%40s%9.2f%18s\n", r.getRentalRateId(), r.getCarCategory().getCarCategory(), r.getName(), r.getRate(), r.getValidityPeriod());
        });
        System.out.print("\nEnter rate ID to view> ");
        Long rateId = sc.nextLong();
        sc.nextLine();
        try {
            RentalRate rate = rentalRateSessionBeanRemote.retrieveRentalRateById(rateId);
            System.out.printf("%2s%20s%40s%9s%18s\n", "ID", "Category", "Name", "Rate", "Valid Days");
            System.out.printf("%2s%20s%40s%9.2f%18s\n", rate.getRentalRateId(), rate.getCarCategory().getCarCategory(), rate.getName(), rate.getRate(), rate.getValidityPeriod());

            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - -");
            System.out.println("1: Update Rate");
            System.out.println("2: Delete/Disable Rate");
            System.out.println("3: Back");
            System.out.print("> ");

            response = sc.nextInt();
            if (response == 1) {
                doUpdateRate(rate);
            } else if (response == 2) {
                doDeleteRate(rate);
            }
        } catch (RentalRateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doDeleteRate(RentalRate rate) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        System.out.println("*** Operations Module :: View Rate Details :: Delete Rate ***\n");

        System.out.printf("%2s%20s%40s%9s%18s\n", "ID", "Category", "Name", "Rate", "Valid Days");
        System.out.printf("%2s%20s%40s%9.2f%18s\n", rate.getRentalRateId(), rate.getCarCategory().getCarCategory(), rate.getName(), rate.getRate(), rate.getValidityPeriod());
        System.out.print("\nConfirm delete rate? (Enter 'Y' to confirm)> ");

        input = sc.nextLine().trim();
        if (input.toLowerCase().equals("y")) {
            try {
                Integer result = rentalRateSessionBeanRemote.deleteRate(rate.getRentalRateId());
                if (result == 0) {
                    System.out.println("Rate deleted!");
                } else if (result == 1) {
                    System.out.println("Rate in use. Rate disabled instead.");
                }
            } catch (RentalRateNotFoundException ex) {
                System.out.println("Unknown error occure deleting rate. " + ex.getMessage());
            }
        } else {
            System.out.println("Delete cancelled");
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doUpdateRate(RentalRate rate) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        Long carCategoryId = rate.getCarCategory().getCarCategoryId();
        System.out.println("*** Operations Module :: View Rate Details :: Update Rate ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories:");
        System.out.printf("%11s%20s\n", "Category ID", "Category Name");
        categories.forEach((cat) -> {
            System.out.printf("%11s%20s\n", cat.getCarCategoryId().toString(), cat.getCarCategory());
        });
        System.out.print("Enter new car category ID (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            carCategoryId = Long.parseLong(input);
        }

        System.out.print("\nEnter new rate name (Leave blank if no change> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            rate.setName(input);
        }
        System.out.print("Enter new rate amount (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            rate.setRate(new BigDecimal(input));
        }
        while (true) {
            System.out.print("Enter rate type (0: Normal, 1: Promo) (Leave blank if no change)> ");
            input = sc.nextLine().trim();
            if (input.length() > 0) {
                if (input.equals("0")) {
                    rate.setIsPromo(false);
                    break;
                } else if (input.equals(1)) {
                    rate.setIsPromo(true);
                    break;
                } else {
                    System.out.println("Invalid option, please try again.");
                }
            } else {
                break;
            }
        }
        System.out.print("Update valid days? (Enter 'Y' to update, else leave blank)> ");
        input = sc.nextLine().trim();
        if (input.toLowerCase().equals("y")) {
            Integer day;
            rate.getValidityPeriod().clear();
            while (true) {
                System.out.print("Enter valid days (1: Monday ... 7: Sunday, -1 to complete)> ");
                day = sc.nextInt();
                if (day == -1) {
                    break;
                } else {
                    rate.getValidityPeriod().add(day);
                    System.out.println(rate.getValidityPeriod());
                }
            }
            sc.nextLine();
        }
        try {
            rate = rentalRateSessionBeanRemote.updateRate(rate, carCategoryId);
            System.out.println("\nRate updated! New rate details:");
            System.out.printf("%2s%20s%40s%9s%18s\n", "ID", "Category", "Name", "Rate", "Valid Days");
            System.out.printf("%2s%20s%40s%9.2f%18s\n", rate.getRentalRateId(), rate.getCarCategory().getCarCategory(), rate.getName(), rate.getRate(), rate.getValidityPeriod());
        } catch (CarCategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

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
