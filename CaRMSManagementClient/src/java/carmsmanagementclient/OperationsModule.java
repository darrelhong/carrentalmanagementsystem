package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRecordSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
import entity.TransitDispatchRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.EmployeeType;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelDisabledException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.EntityDisabledException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightsException;
import util.exception.OutletNotFoundException;
import util.exception.TransitDispatchRecordNotFoundException;
import util.exception.TransitNotAssignedException;
import util.exception.UnknownPersistenceException;
import util.helper.Print;

public class OperationsModule {
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CarSessionBeanRemote carSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private RentalRecordSessionBeanRemote rentalRecordSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;

    private Employee currentEmployee;

    public OperationsModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationsModule(Employee currentEmployee, CarModelSessionBeanRemote cmsbr,
            CarSessionBeanRemote csbr, CarCategorySessionBeanRemote ccsbr,
            OutletSessionBeanRemote osbr, RentalRecordSessionBeanRemote rrsbr,
            EmployeeSessionBeanRemote esbr) {
        this();
        this.currentEmployee = currentEmployee;
        this.carModelSessionBeanRemote = cmsbr;
        this.carSessionBeanRemote = csbr;
        this.carCategorySessionBeanRemote = ccsbr;
        this.outletSessionBeanRemote = osbr;
        this.rentalRecordSessionBeanRemote = rrsbr;
        this.employeeSessionBeanRemote = esbr;
    }

    public void menuOperations() throws InvalidAccessRightsException {
        if (currentEmployee.getEmployeeType() == EmployeeType.ADMIN
                || currentEmployee.getEmployeeType() == EmployeeType.OPERATIONS) {

            Scanner sc = new Scanner(System.in);
            Integer response = 0;

            while (true) {
                System.out.println("*** CaRMS Management Client :: Operations Menu ***\n");
                System.out.println("1: Create New Car Model  |  5: Create New Car");
                System.out.println("2: View All Car Models   |  6: View All Cars");
                System.out.println("3: Update Car Model      |  7: View Car Details");
                System.out.println("4: Delete Car Model");
                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - -");
                System.out.println("8: View transit driver dispatch records for current day reservations");
                System.out.println("9: Assign Transit Driver");
                System.out.println("10: Update transit as completed");
                System.out.println("11: (testing only) Allocate cars for chosen day");
                System.out.println("12: (testing only) View all transit dispatch records for chosen day (all outlets)");
                System.out.println("13: (testing only) View transit dispatch records for chosen day (current outlet)");
                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - -");
                System.out.println("14: Back\n");
                response = 0;

                OUTER:
                while (response < 1 || response > 14) {
                    System.out.print("> ");
                    response = sc.nextInt();
                    if (response == 1) {
                        doCreateNewCarModel();
                    } else if (response == 2) {
                        doViewAllCarModels();
                    } else if (response == 3) {
                        doUpdateCarModel();
                    } else if (response == 4) {
                        doDeleteCarModel();
                    } else if (response == 5) {
                        doCreateNewCar();
                    } else if (response == 6) {
                        doViewAllCars();
                    } else if (response == 7) {
                        doViewCarDetails();
                    } else if (response == 8) {
                        doViewTodayTransitRecords();
                    } else if (response == 9) {
                        doAssignTransitDriver();
                    } else if (response == 10) {
                        doUpdateTransitAsCompleted();
                    } else if (response == 11) {
                        doAllocateCars();
                    } else if (response == 12) {
                        doViewAllTDRs();
                    } else if (response == 13) {
                        doViewTDR();
                    } else if (response == 14) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }
                if (response == 14) {
                    break;
                }
            }
        } else {
            throw new InvalidAccessRightsException("Operations menu requires ADMIN or OPERATIONS rights.\n");
        }
    }

    private void doCreateNewCarModel() {
        Scanner sc = new Scanner(System.in);
        CarModel newCarModel = new CarModel();
        System.out.println("*** Operations Menu :: Create New Car Model ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories:");
        Print.printListCarCategories(categories);
        System.out.print("Enter new car model category ID> ");
        Long categoryId = Long.parseLong(sc.nextLine().trim());
        System.out.print("Enter new car make> ");
        newCarModel.setMake(returnNonEmptyString(sc));
        System.out.print("Enter new car model> ");
        newCarModel.setModel(returnNonEmptyString(sc));
        Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(newCarModel);

        if (constraintViolations.isEmpty()) {
            try {
                newCarModel = carModelSessionBeanRemote.createNewCarModel(newCarModel, categoryId);
                System.out.println("\nNew model created!");
                Print.printCarModel(newCarModel);
    //            System.out.println("New car model created. ID = " + newCarModel.getCarModelId() + ": " + newCarModel.getMake() + " " + newCarModel.getModel() + "\n");
            } catch (CarCategoryNotFoundException | UnknownPersistenceException | InputDataValidationException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            showInputDataValidationErrorsForCarModel(constraintViolations);
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
    
    private void showInputDataValidationErrorsForCarModel(Set<ConstraintViolation<CarModel>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void doViewAllCarModels() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Operations Module :: View All Car Models ***\n");

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();
        Print.printListCarModels(carModels);
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doUpdateCarModel() {
        Scanner sc = new Scanner(System.in);
        CarModel carModelToUpdate;
        String input = "";
        Long categoryId;
        System.out.println("*** Operations Module :: Update Car Model ***\n");

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();
        System.out.println("List of car models");
        Print.printListCarModels(carModels);

        System.out.print("\n Enter model ID to update> ");
        Long modelId = Long.parseLong(sc.nextLine().trim());
        try {
            carModelToUpdate = carModelSessionBeanRemote.retrieveCarModelById(modelId);
            categoryId = carModelToUpdate.getCarCategory().getCarCategoryId();
        } catch (CarModelNotFoundException ex) {
            System.out.println(ex.getMessage());
            return;
        }

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories:");
        Print.printListCarCategories(categories);

        System.out.print("Enter new car category ID (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            categoryId = Long.parseLong(input);
        }
        System.out.print("Enter new car make (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            carModelToUpdate.setMake(input);
        }
        System.out.print("Enter new car model (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            carModelToUpdate.setModel(input);
        }
        try {
            carModelToUpdate = carModelSessionBeanRemote.updateCarModel(carModelToUpdate, modelId, categoryId);
            System.out.println("\nCar model updated! New car model details:");
            Print.printCarModel(carModelToUpdate);
        } catch (CarCategoryNotFoundException | CarModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doDeleteCarModel() {
        Scanner sc = new Scanner(System.in);
        String input = "";
        System.out.println("*** Operations Module :: Update Car Model ***\n");

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();
        System.out.println("List of car models:");
        Print.printListCarModels(carModels);

        System.out.print("\nEnter model ID to delete/disable> ");
        Long modelId = Long.parseLong(sc.nextLine().trim());
        try {
            CarModel toDelete = carModelSessionBeanRemote.retrieveCarModelById(modelId);
            Print.printCarModel(toDelete);
        } catch (CarModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Confirm delete car model? (Enter 'Y' to confirm)");
        input = sc.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            try {
                Integer result = carModelSessionBeanRemote.deleteCarModel(modelId);
                if (result == 0) {
                    System.out.println("Car model ID " + modelId + " deleted");
                } else if (result == 1) {
                    System.out.println("Car entities or rental records uisng car model exist. Car model ID " + modelId + " disabled instead");
                }
            } catch (CarModelNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled");
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doCreateNewCar() {
        Scanner sc = new Scanner(System.in);
        Car newCar = new Car();
        System.out.println("*** Operations Module :: Create New Car ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories:");
        Print.printListCarCategories(categories);

        System.out.print("\nEnter new car category ID> ");
        Long categoryId = Long.parseLong(sc.nextLine().trim());

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveCarModelsByCategory(categoryId);
        System.out.println("List of car models:");
        Print.printListCarModels(carModels);
        System.out.print("\nEnter new car model ID> ");
        Long modelId = Long.parseLong(sc.nextLine().trim());

        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        System.out.println("List of outlets:");
        Print.printListOutlets(outlets);
        System.out.print("\nEnter new car outlet ID> ");
        Long outletId = Long.parseLong(sc.nextLine().trim());

        System.out.print("Enter new car license plate> ");
        newCar.setLicensePlate(returnNonEmptyString(sc));
        System.out.print("Enter new car colour> ");
        newCar.setColour(returnNonEmptyString(sc));
        Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

        if (constraintViolations.isEmpty()) {
            try {
                newCar = carSessionBeanRemote.createNewCar(newCar, categoryId, modelId, outletId);
                System.out.println("\nNew car created!");
                Print.printCar(newCar);
            } catch (UnknownPersistenceException | EntityDisabledException | CarCategoryNotFoundException
                    | CarModelNotFoundException | OutletNotFoundException | CarModelDisabledException | InputDataValidationException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            showInputDataValidationErrorsForCar(constraintViolations);
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
    
    private void showInputDataValidationErrorsForCar(Set<ConstraintViolation<Car>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

    private void doViewAllCars() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Operations Module :: View All Cars ***\n");

        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        Print.printListCars(cars);

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doViewCarDetails() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        System.out.println("*** Operations Module :: View Car Details ***\n");

        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        Print.printListCars(cars);

        System.out.print("\nEnter car ID to view> ");
        Long carId = Long.parseLong(sc.nextLine().trim());
        try {
            Car car = carSessionBeanRemote.retrieveCarById(carId);
            Print.printCar(car);
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - -");
            System.out.println("1: Update Car");
            System.out.println("2: Delete/Disable Car");
            System.out.println("3: Back");
            System.out.print("> ");

            response = sc.nextInt();
            if (response == 1) {
                doUpdateCar(car);
            } else if (response == 2) {
                doDeleteCar(car);
            }
        } catch (CarNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doDeleteCar(Car car) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        System.out.println("\n*** Operations Menu :: View Car Details :: Delete/Disable Car ***\n");

        Print.printCar(car);
        System.out.print("\nConfirm delete car? (Enter 'Y' to confirm)> ");
//        System.out.println("Confirm delete car: " + car.getCarModel().getMake()
//                + " " + car.getCarModel().getModel()
//                + " " + car.getLicensePlate() + "? (Press 'Y' to confirm)");
        input = sc.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            try {
                Integer result = carSessionBeanRemote.deleteCar(car.getCarId());
                if (result == 0) {
                    System.out.println("\nCar " + car.getCarModel().getMake()
                            + " " + car.getCarModel().getModel()
                            + " " + car.getLicensePlate() + " deleted");
                } else if (result == 1) {
                    System.out.println("\nCar in use. Car " + car.getCarModel().getMake()
                            + " " + car.getCarModel().getModel()
                            + " " + car.getLicensePlate() + " disabled instead");
                }
            } catch (CarNotFoundException ex) {
                System.out.println("Unknown error occured deleting Car. " + ex.getMessage());
            }
        } else {
            System.out.println("\nDeletion cancelled");
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doUpdateCar(Car car) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        Long carCategoryId = car.getCarCategory().getCarCategoryId();
        Long modelId = car.getCarModel().getCarModelId();
        Long outletId = car.getOutlet().getOutletId();
        System.out.println("*** Operations Module :: View Car Details :: Update Car ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories: ");
        Print.printListCarCategories(categories);

        System.out.print("Enter new car category ID (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            carCategoryId = Long.parseLong(input);
        }

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveCarModelsByCategory(carCategoryId);
        System.out.println("List of car models: ");
        Print.printListCarModels(carModels);

        System.out.print("Enter new car model ID (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            modelId = Long.parseLong(input);
        }

        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        System.out.println("List of outlets: ");
        Print.printListOutlets(outlets);

        System.out.print("\nEnter new car outlet ID (Leave blank if no change> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            outletId = Long.parseLong(input);
        }

        System.out.print("Enter new car license plate (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            car.setLicensePlate(input);
        }

        System.out.print("Enter new car colour (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            car.setColour(input);
        }
        try {
            car = carSessionBeanRemote.updateCar(car, carCategoryId, modelId, outletId);
            System.out.println("\nCar updated! New car details:");
            Print.printCar(car);
        } catch (CarCategoryNotFoundException | CarModelNotFoundException | OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doViewTodayTransitRecords() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Operations Menu :: View Today's Transit Records ***\n");

        List<TransitDispatchRecord> tdrs = rentalRecordSessionBeanRemote.retrieveCurrentDayDispatchRecords(currentEmployee, new Date());
        Print.printListTDR(tdrs);

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doAssignTransitDriver() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Operations Menu :: Assign Transit Driver ***\n");

        List<TransitDispatchRecord> tdrs = rentalRecordSessionBeanRemote.retrieveCurrentDayDispatchRecords(currentEmployee, new Date());
        Print.printListTDR(tdrs);

        System.out.print("Enter TDR ID> ");
        Long tdrId = Long.parseLong(sc.nextLine().trim());

        List<Employee> outletEmployees = employeeSessionBeanRemote.retrieveAllEmployeesByOutlet(currentEmployee.getOutlet().getOutletId());
        Print.printListEmployees(outletEmployees);

        System.out.print("\nEnter employee ID to assign> ");
        Long employeeId = Long.parseLong(sc.nextLine().trim());

        try {
            TransitDispatchRecord result = rentalRecordSessionBeanRemote.assignEmployeeToTDR(tdrId, employeeId);
            System.out.println("Assignment success!");
            Print.printTDR(result);
        } catch (EmployeeNotFoundException | TransitDispatchRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doUpdateTransitAsCompleted() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Operations Menu :: Update Transit as Completed ***\n");

        List<TransitDispatchRecord> tdrs = rentalRecordSessionBeanRemote.retrieveCurrentDayDispatchRecords(currentEmployee, new Date());
        Print.printListTDR(tdrs);

        System.out.print("Enter TDR ID to mark as completed> ");
        Long tdrId = Long.parseLong(sc.nextLine().trim());

        try {
            TransitDispatchRecord result = rentalRecordSessionBeanRemote.markTransitAsCompleted(tdrId);
            System.out.println("Transit updated as completed!");
            Print.printTDR(result);
        } catch (TransitDispatchRecordNotFoundException | TransitNotAssignedException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doAllocateCars() {
        Scanner sc = new Scanner(System.in);
        Date time;
        System.out.println("*** Operations Module :: 11: (testing only) Allocate cars ***\n");

        while (true) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
                System.out.print("Enter date (dd/mm/yyyy hh:mm AM/PM)> ");
                time = sdf.parse(sc.nextLine().trim());

                rentalRecordSessionBeanRemote.allocateCars(time);
                System.out.println("Successful allocation!");
                break;
            } catch (ParseException ex) {
                System.out.println("Invalid date time input, please try again");
            }
        }
        rentalRecordSessionBeanRemote.allocateCars(time);
        System.out.println("Successful allocation!");
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doViewAllTDRs() {
        Scanner sc = new Scanner(System.in);
        Date time;
        System.out.println("*** Operations Module :: 12: (testing only) View all transit dispatch records for chosen day ***\n");

        while (true) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
                System.out.print("Enter date (dd/mm/yyyy hh:mm AM/PM)> ");
                time = sdf.parse(sc.nextLine().trim());
                break;
            } catch (ParseException ex) {
                System.out.println("Invalid date time input, please try again");
            }
        }
        List<TransitDispatchRecord> tdrs = rentalRecordSessionBeanRemote.retrieveTransitDispatchRecords(time);
        Print.printListTDR(tdrs);

        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
    
    private void doViewTDR() {
        Scanner sc = new Scanner(System.in);
        Date time;
        System.out.println("*** Operations Module :: 12: (testing only) View transit dispatch records for chosen day (current outlet) ***\n");

        while (true) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
                System.out.print("Enter date (dd/mm/yyyy hh:mm AM/PM)> ");
                time = sdf.parse(sc.nextLine().trim());
                break;
            } catch (ParseException ex) {
                System.out.println("Invalid date time input, please try again");
            }
        }
        List<TransitDispatchRecord> tdrs = rentalRecordSessionBeanRemote.retrieveCurrentDayDispatchRecords(currentEmployee, time);
        Print.printListTDR(tdrs);

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
