package util.helper;

import java.util.List;
import ws.client.Car;
import ws.client.CarCategory;
import ws.client.CarModel;
import ws.client.Outlet;
import ws.client.RentalRecord;

/**
 *
 * @author darre
 */
public class Print {
    public static void printListRentalRecords(List<RentalRecord> records) {
        
        System.out.printf("%10s%9s%20s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", "Booking ID", "Amount", "Category", "Make", "Model", "Start", "End", "From", "To", "Paid", "PickedUp", "Returned", "Cancelled");
        records.forEach((r) -> {
            String make = r.getCarModel() != null ? r.getCarModel().getMake() : "";
            String model = r.getCarModel() != null ? r.getCarModel().getModel() : "";
            String category = r.getCarCategory() != null ? r.getCarCategory().getCarCategory() : r.getCarModel().getCarCategory().getCarCategory();
            System.out.printf("%10s%9.2f%20s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", r.getRentalRecordId(), r.getAmount(), category, make, model, r.getStartDateTime(), r.getEndDateTime(), r.getFromOutlet().getAddress(), r.getToOutlet().getAddress(), r.isPaid(), r.isPickedUp(), r.isReturned(), r.isCancelled());
        });
    }

    public static void printRentalRecord(RentalRecord r) {
        System.out.printf("%10s%9s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", "Booking ID", "Amount", "Make", "Model", "Start", "End", "From", "To", "Paid", "PickedUp", "Returned", "Cancelled");
        System.out.printf("%10s%9.2f%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", r.getRentalRecordId(), r.getAmount(), r.getCarModel().getMake(), r.getCarModel().getModel(), r.getStartDateTime(), r.getEndDateTime(), r.getFromOutlet().getAddress(), r.getToOutlet().getAddress(), r.isPaid(), r.isPickedUp(), r.isReturned(), r.isCancelled());
    }

    public static void printListCarCategories(List<CarCategory> categories) {
        System.out.printf("%11s%20s\n", "Category ID", "Category Name");
        categories.forEach((cat) -> {
            System.out.printf("%11s%20s\n", cat.getCarCategoryId().toString(), cat.getCarCategory());
        });
    }

    public static void printCarModel(CarModel carModel) {
        System.out.printf("%8s%20s%20s%20s\n", "Model ID", "Make", "Model", "Category");
        System.out.printf("%8s%20s%20s%20s\n", carModel.getCarModelId(), carModel.getMake(), carModel.getModel(), carModel.getCarCategory().getCarCategory());
    }

    public static void printListCarModels(List<CarModel> carModels) {
        System.out.printf("%8s%20s%20s%20s\n", "Model ID", "Make", "Model", "Category");
        carModels.forEach((model) -> {
            System.out.printf("%8s%20s%20s%20s\n", model.getCarModelId(), model.getMake(), model.getModel(), model.getCarCategory().getCarCategory());
        });
    }

    public static void printListOutlets(List<Outlet> outlets) {
        System.out.println("List of outlets:");
        System.out.printf("%9s%32s\n", "Outlet ID", "Outlet Address");
        outlets.forEach((outlet) -> {
            System.out.printf("%9s%32s\n", outlet.getOutletId(), outlet.getAddress());
        });
    }

    public static void printCar(Car car) {
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
        String status = car.isAvailabilityStatus()? "UNAVAIL" : "AVAIL";
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(), status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(), car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(), car.getOutlet().getOutletId());
    }

    public static void printListCars(List<Car> cars) {
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
        cars.forEach((car) -> {
            String status = car.isAvailabilityStatus()? "UNAVAIL" : "AVAIL";
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(),
                    status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(),
                    car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(),
                    car.getOutlet().getOutletId());
        });
    }
}
