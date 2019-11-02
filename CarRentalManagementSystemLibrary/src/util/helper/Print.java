package util.helper;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import entity.RentalRate;
import entity.RentalRecord;
import java.util.List;

/**
 *
 * @author darre
 */
public class Print {

    public static void printListRentalRecords(List<RentalRecord> records) {
        System.out.printf("%10s%9s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", "Booking ID", "Amount", "Make", "Model", "Start", "End", "From", "To", "Paid", "PickedUp", "Returned", "Cancelled");
        records.forEach((r) -> {
            System.out.printf("%10s%9.2f%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", r.getRentalRecordId(), r.getAmount(), r.getCarModel().getMake(), r.getCarModel().getModel(), r.getStartDateTime(), r.getEndDateTime(), r.getFromOutlet().getAddress(), r.getToOutlet().getAddress(), r.getPaid(), r.getPickedUp(), r.getReturned(), r.getCancelled());
        });
    }

    public static void printRentalRecord(RentalRecord r) {
        System.out.printf("%10s%9s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", "Booking ID", "Amount", "Make", "Model", "Start", "End", "From", "To", "Paid", "PickedUp", "Returned", "Cancelled");
        System.out.printf("%10s%9.2f%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", r.getRentalRecordId(), r.getAmount(), r.getCarModel().getMake(), r.getCarModel().getModel(), r.getStartDateTime(), r.getEndDateTime(), r.getFromOutlet().getAddress(), r.getToOutlet().getAddress(), r.getPaid(), r.getPickedUp(), r.getReturned(), r.getCancelled());
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
        String status = car.getAvailabilityStatus() ? "UNAVAIL" : "AVAIL";
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(), status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(), car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(), car.getOutlet().getOutletId());
    }

    public static void printListCars(List<Car> cars) {
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
        cars.forEach((car) -> {
            String status = car.getAvailabilityStatus() ? "UNAVAIL" : "AVAIL";
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(),
                    status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(),
                    car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(),
                    car.getOutlet().getOutletId());
        });
    }

    public static void printRate(RentalRate rate) {
        System.out.printf("%2s%20s%40s%9s%8s%31s%31s\n", "ID", "Category", "Name", "Rate", "Type", "Start Date", "End Date");
        String rateType = rate.getIsPromo() ? "PROMO" : "NORMAL";
        System.out.printf("%2s%20s%40s%9.2f%8s%31s%31s\n", rate.getRentalRateId(), rate.getCarCategory().getCarCategory(), rate.getName(), rate.getRate(), rateType, rate.getStartDate(), rate.getEndDate());
    }

    public static void printListRates(List<RentalRate> rates) {
        System.out.printf("%2s%20s%40s%9s%8s%31s%31s\n", "ID", "Category", "Name", "Rate", "Type", "Start Date", "End Date");
        rates.forEach((rate) -> {
            String rateType = rate.getIsPromo() ? "PROMO" : "NORMAL";
            System.out.printf("%2s%20s%40s%9.2f%8s%31s%31s\n", rate.getRentalRateId(), rate.getCarCategory().getCarCategory(), rate.getName(), rate.getRate(), rateType, rate.getStartDate(), rate.getEndDate());
        });
    }
}
