package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeType;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.EntityDisabledException;
import util.exception.InvalidAccessRightsException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
public class OperationsModule {

    private CarSessionBeanRemote carSessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;

    private Employee currentEmployee;

    public OperationsModule() {
    }

    public OperationsModule(Employee currentEmployee, CarModelSessionBeanRemote cmsbr,
            CarSessionBeanRemote csbr, CarCategorySessionBeanRemote ccsbr,
            OutletSessionBeanRemote osbr) {
        this.currentEmployee = currentEmployee;
        this.carModelSessionBeanRemote = cmsbr;
        this.carSessionBeanRemote = csbr;
        this.carCategorySessionBeanRemote = ccsbr;
        this.outletSessionBeanRemote = osbr;
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
                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - -");
                System.out.println("11: Back\n");
                response = 0;

                OUTER:
                while (response < 1 || response > 11) {
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
                    } else if (response == 11) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                }
                if (response == 11) {
                    break;
                }
            }
        } else {
            throw new InvalidAccessRightsException("Operations menu requires ADMIN or OPERATIONS rights.");
        }
    }

    private void doCreateNewCarModel() {
        Scanner sc = new Scanner(System.in);
        CarModel newCarModel = new CarModel();
        System.out.println("*** Operations Menu :: Create New Car Model ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("List of car categories:");
        System.out.printf("%11s%20s\n", "Category ID", "Category Name");
        categories.forEach((cat) -> {
            System.out.printf("%11s%20s\n", cat.getCarCategoryId().toString(), cat.getCarCategory());
        });
        System.out.print("Enter new car model category ID> ");
        Long categoryId = Long.parseLong(sc.nextLine().trim());
        System.out.print("Enter new car make> ");
        newCarModel.setMake(sc.nextLine().trim());
        System.out.print("Enter new car model> ");
        newCarModel.setModel(sc.nextLine().trim());

        try {
            newCarModel = carModelSessionBeanRemote.createNewCarModel(newCarModel, categoryId);
            System.out.println("New car model created. ID = " + newCarModel.getCarModelId() + ": " + newCarModel.getMake() + " " + newCarModel.getModel() + "\n");
        } catch (CarCategoryNotFoundException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    private void doViewAllCarModels() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Operations Module :: View All Car Models ***\n");

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();
        System.out.printf("%8s%20s%20s%20s\n", "Model ID", "Make", "Model", "Category");
        carModels.forEach((model) -> {
            System.out.printf("%8s%20s%20s%20s\n", model.getCarModelId(), model.getMake(), model.getModel(), model.getCarCategory().getCarCategory());
        });
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
        System.out.printf("%8s%20s%20s%20s\n", "Model ID", "Make", "Model", "Category");
        carModels.forEach((model) -> {
            System.out.printf("%8s%20s%20s%20s\n", model.getCarModelId(), model.getMake(), model.getModel(), model.getCarCategory().getCarCategory());
        });
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
        System.out.printf("%11s%20s\n", "Category ID", "Category Name");
        categories.forEach((cat) -> {
            System.out.printf("%11s%20s\n", cat.getCarCategoryId().toString(), cat.getCarCategory());
        });
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
            System.out.println("Car model ID = " + carModelToUpdate.getCarModelId() + " updated: " + carModelToUpdate.getMake() + " " + carModelToUpdate.getModel() + " " + carModelToUpdate.getCarCategory().getCarCategory());
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
        System.out.printf("%8s%20s%20s%20s\n", "Model ID", "Make", "Model", "Category");
        carModels.forEach((model) -> {
            System.out.printf("%8s%20s%20s%20s\n", model.getCarModelId(), model.getMake(), model.getModel(), model.getCarCategory().getCarCategory());
        });
        System.out.print("\nEnter model ID to delete/disable> ");
        Long modelId = Long.parseLong(sc.nextLine().trim());
        System.out.println("Confirm delete car model ID " + modelId + "? (Enter 'Y' to confirm)");
        input = sc.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            try {
                Integer result = carModelSessionBeanRemote.deleteCarModel(modelId);
                if (result == 0) {
                    System.out.println("Car model ID " + modelId + " deleted");
                } else if (result == 1) {
                    System.out.println("Car entities exist. Car model ID " + modelId + " disabled instead");
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
        System.out.printf("%11s%20s\n", "Category ID", "Category Name");
        categories.forEach((cat) -> {
            System.out.printf("%11s%20s\n", cat.getCarCategoryId().toString(), cat.getCarCategory());
        });
        System.out.print("\nEnter new car category ID> ");
        Long categoryId = Long.parseLong(sc.nextLine().trim());

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveCarModelsByCategory(categoryId);
        System.out.println("List of car models:");
        System.out.printf("%8s%20s%20s%20s\n", "Model ID", "Make", "Model", "Category");
        carModels.forEach((model) -> {
            System.out.printf("%8s%20s%20s%20s\n", model.getCarModelId(), model.getMake(), model.getModel(), model.getCarCategory().getCarCategory());
        });
        System.out.print("\nEnter new car model ID> ");
        Long modelId = Long.parseLong(sc.nextLine().trim());

        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        System.out.println("List of outlets:");
        System.out.printf("%9s%32s\n", "Outlet ID", "Outlet Address");
        outlets.forEach((outlet) -> {
            System.out.printf("%9s%32s\n", outlet.getOutletId(), outlet.getAddress());
        });
        System.out.print("\nEnter new car outlet ID> ");
        Long outletId = Long.parseLong(sc.nextLine().trim());

        System.out.print("Enter new car license plate> ");
        newCar.setLicensePlate(sc.nextLine().trim());
        System.out.print("Enter new car colour> ");
        newCar.setColour(sc.nextLine().trim());

        try {
            newCar = carSessionBeanRemote.createNewCar(newCar, categoryId, modelId, outletId);
            System.out.println("New car created. ID = " + newCar.getCarId() + " "
                    + newCar.getCarCategory().getCarCategory() + " " + newCar.getColour()
                    + " " + newCar.getCarModel().getMake()
                    + " " + newCar.getCarModel().getModel()
                    + " Plate No." + newCar.getLicensePlate()
                    + " at " + newCar.getOutlet().getAddress());
        } catch (UnknownPersistenceException | EntityDisabledException | CarCategoryNotFoundException
                | CarModelNotFoundException | OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doViewAllCars() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Operations Module :: View All Cars ***\n");

        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
        cars.forEach((car) -> {
            String status = car.getDisabled() ? "UNAVAIL" : "AVAIL";
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(),
                    status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(),
                    car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(),
                    car.getOutlet().getOutletId());
        });
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doViewCarDetails() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        System.out.println("*** Operations Module :: View Car Details ***\n");

        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
        cars.forEach((car) -> {
            String status = car.getDisabled() ? "UNAVAIL" : "AVAIL";
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(),
                    status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(),
                    car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(),
                    car.getOutlet().getOutletId());
        });
        System.out.print("\nEnter car ID to view> ");
        Long carId = Long.parseLong(sc.nextLine().trim());
        try {
            Car car = carSessionBeanRemote.retrieveCarById(carId);
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
            String carStatus = car.getDisabled() ? "UNAVAIL" : "AVAIL";
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(),
                    carStatus, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(),
                    car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(),
                    car.getOutlet().getOutletId());
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
        System.out.println("Confirm delete car: " + car.getCarModel().getMake()
                + " " + car.getCarModel().getModel()
                + " " + car.getLicensePlate() + "? (Press 'Y' to confirm)");
        input = sc.nextLine().trim().toLowerCase();
        if (input.equals("y")) {
            try {
                Integer result = carSessionBeanRemote.deleteCar(car.getCarId());
                if (result == 0) {
                    System.out.println("Car " + car.getCarModel().getMake()
                            + " " + car.getCarModel().getModel()
                            + " " + car.getLicensePlate() + " deleted");
                } else if (result == 1) {
                    System.out.println("Car in use. Car " + car.getCarModel().getMake()
                            + " " + car.getCarModel().getModel()
                            + " " + car.getLicensePlate() + " disabled instead");
                }
            } catch (CarNotFoundException ex) {
                System.out.println("Unknown error occured deleting Car. " + ex.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled");
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doUpdateCar(Car car) {
        Scanner sc = new Scanner(System.in);
        String input = "";
        Long categoryId = car.getCarCategory().getCarCategoryId();
        Long modelId = car.getCarModel().getCarModelId();
        Long outletId = car.getOutlet().getOutletId();
        System.out.println("*** Operations Module :: View Car Details :: Update Car ***\n");

        List<CarCategory> categories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.printf("%11s%20s\n", "Category ID", "Category Name");
        categories.forEach((cat) -> {
            System.out.printf("%11s%20s\n", cat.getCarCategoryId().toString(), cat.getCarCategory());
        });
        System.out.print("Enter new car category ID (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            categoryId = Long.parseLong(input);
        }

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveCarModelsByCategory(categoryId);
        System.out.printf("%8s%20s%20s%20s\n", "Model ID", "Make", "Model", "Category");
        carModels.forEach((model) -> {
            System.out.printf("%8s%20s%20s%20s\n", model.getCarModelId(), model.getMake(), model.getModel(), model.getCarCategory().getCarCategory());
        });
        System.out.print("Enter new car model ID (Leave blank if no change)> ");
        input = sc.nextLine().trim();
        if (input.length() > 0) {
            modelId = Long.parseLong(input);
        }

        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        System.out.printf("%9s%32s\n", "Outlet ID", "Outlet Address");
        outlets.forEach((outlet) -> {
            System.out.printf("%9s%32s\n", outlet.getOutletId(), outlet.getAddress());
        });
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
            car = carSessionBeanRemote.updateCar(car, categoryId, modelId, outletId);
            System.out.println("Car updated! New car details");
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
            String carStatus = car.getDisabled() ? "UNAVAIL" : "AVAIL";
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(),
                    carStatus, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(),
                    car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(),
                    car.getOutlet().getOutletId());
        } catch (CarCategoryNotFoundException | CarModelNotFoundException | OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }
}
