package util.helper;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
import entity.RentalRate;
import entity.RentalRecord;
import entity.TransitDispatchRecord;
import java.util.List;

public class Print {

    public static void printListEmployees(List<Employee> employees) {
        System.out.printf("%11s%20s%11s%20s\n", "Employee ID", "Name", "Role", "Outlet");
        employees.forEach((e) -> {
            System.out.printf("%11s%20s%11s%20s\n", e.getEmployeeId(), e.getName(), e.getEmployeeType(), e.getOutlet().getAddress());
        });
    }

    public static void printEmployee(Employee e) {
        System.out.printf("%11s%20s%11s%20s\n", "Employee ID", "Name", "Role", "Outlet");
        System.out.printf("%11s%20s%11s%20s\n", e.getEmployeeId(), e.getName(), e.getEmployeeType(), e.getOutlet().getAddress());
    }

    public static void printListTDR(List<TransitDispatchRecord> tdrs) {
        System.out.printf("%6s%20s%20s%15s%20s%10s%32s\n", "TDR ID", "From Outlet", "To Outlet", "Car Id", "Assigned Employee", "Completed", "Booking Start");
        tdrs.forEach((tdr) -> {
            String employee = tdr.getEmployee() != null ? tdr.getEmployee().getName() : "not assigned";
            String carPlate = tdr.getRentalRecord().getCar() != null ? tdr.getRentalRecord().getCar().getLicensePlate() : "not allocated";
            System.out.printf("%6s%20s%20s%15s%20s%10s%32s\n", tdr.getId(), tdr.getFromOutlet().getAddress(), tdr.getRentalRecord().getFromOutlet().getAddress(), carPlate, employee, tdr.getCompleted(), tdr.getRentalRecord().getStartDateTime());
        });
    }

    public static void printTDR(TransitDispatchRecord tdr) {
        System.out.printf("%6s%20s%20s%15s%20s%10s%32s\n", "TDR ID", "From Outlet", "To Outlet", "Car Id", "Assigned Employee", "Completed", "Booking Start");
        String employee = tdr.getEmployee() != null ? tdr.getEmployee().getName() : "not assigned";
        String carPlate = tdr.getRentalRecord().getCar() != null ? tdr.getRentalRecord().getCar().getLicensePlate() : "not allocated";
        System.out.printf("%6s%20s%20s%15s%20s%10s%32s\n", tdr.getId(), tdr.getFromOutlet().getAddress(), tdr.getRentalRecord().getFromOutlet().getAddress(), carPlate, employee, tdr.getCompleted(), tdr.getRentalRecord().getStartDateTime());
    }

    public static void printListRentalRecords(List<RentalRecord> records) {
        System.out.printf("%10s%9s%20s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", "Booking ID", "Amount", "Category", "Make", "Model", "Start", "End", "From", "To", "Paid", "PickedUp", "Returned", "Cancelled");
        records.forEach((r) -> {
            String make = r.getCarModel() != null ? r.getCarModel().getMake() : "";
            String model = r.getCarModel() != null ? r.getCarModel().getModel() : "";
            String category = r.getCarCategory() != null ? r.getCarCategory().getCarCategory() : r.getCarModel().getCarCategory().getCarCategory();
            System.out.printf("%10s%9.2f%20s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", r.getRentalRecordId(), r.getAmount(), category, make, model, r.getStartDateTime(), r.getEndDateTime(), r.getFromOutlet().getAddress(), r.getToOutlet().getAddress(), r.getPaid(), r.getPickedUp(), r.getReturned(), r.getCancelled());
        });
    }

    public static void printRentalRecord(RentalRecord r) {
        System.out.printf("%10s%9s%20s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", "Booking ID", "Amount", "Category", "Make", "Model", "Start", "End", "From", "To", "Paid", "PickedUp", "Returned", "Cancelled");
        String make = r.getCarModel() != null ? r.getCarModel().getMake() : "";
        String model = r.getCarModel() != null ? r.getCarModel().getModel() : "";
        String category = r.getCarCategory() != null ? r.getCarCategory().getCarCategory() : r.getCarModel().getCarCategory().getCarCategory();
        System.out.printf("%10s%9.2f%20s%20s%20s%31s%31s%32s%32s%6s%9s%9s%10s\n", r.getRentalRecordId(), r.getAmount(), category, make, model, r.getStartDateTime(), r.getEndDateTime(), r.getFromOutlet().getAddress(), r.getToOutlet().getAddress(), r.getPaid(), r.getPickedUp(), r.getReturned(), r.getCancelled());
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
        System.out.printf("%9s%32s%6s%7s\n", "Outlet ID", "Outlet Address", "Opens", "Closes");
        outlets.forEach((outlet) -> {
            String opens = outlet.getOpenTime() != null ? outlet.getOpenTime().toString() : "null";
            String closes = outlet.getCloseTime() != null ? outlet.getCloseTime().toString() : "null";
            System.out.printf("%9s%32s%6s%7s\n", outlet.getOutletId(), outlet.getAddress(), opens, closes);
        });
    }

    public static void printCar(Car car) {
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
        String status = car.getAvailabilityStatus() ? "AVAIL" : "UNAVAIL";
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(), status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(), car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(), car.getOutlet().getOutletId());
    }

    public static void printListCars(List<Car> cars) {
        System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", "Car ID", "Status", "Car Category", "Car Make", "Car Model", "License Plate", "Colour", "Outlet ID");
        cars.forEach((car) -> {
            String status = car.getAvailabilityStatus() ? "AVAIL" : "UNAVAIL";
            System.out.printf("%6s%8s%25s%15s%15s%18s%15s%10s\n", car.getCarId(),
                    status, car.getCarCategory().getCarCategory(), car.getCarModel().getMake(),
                    car.getCarModel().getModel(), car.getLicensePlate(), car.getColour(),
                    car.getOutlet().getOutletId());
        });
    }

    public static void printRate(RentalRate rate) {
        System.out.printf("%2s%20s%40s%9s%8s%31s%31s\n", "ID", "Category", "Name", "Rate", "Type", "Start Date", "End Date");
        String rateType = rate.getIsPromo() ? "PROMO" : "NORMAL";
        String start = rate.getStartDate() != null ? rate.getStartDate().toString() : "null";
        String end = rate.getEndDate() != null ? rate.getEndDate().toString() : "null";
        System.out.printf("%2s%20s%40s%9.2f%8s%31s%31s\n", rate.getRentalRateId(), rate.getCarCategory().getCarCategory(), rate.getName(), rate.getRate(), rateType, start, end);
    }

    public static void printListRates(List<RentalRate> rates) {
        System.out.printf("%2s%20s%40s%9s%8s%31s%31s\n", "ID", "Category", "Name", "Rate", "Type", "Start Date", "End Date");
        rates.forEach((rate) -> {
            String rateType = rate.getIsPromo() ? "PROMO" : "NORMAL";
            String start = rate.getStartDate() != null ? rate.getStartDate().toString() : "null";
            String end = rate.getEndDate() != null ? rate.getEndDate().toString() : "null";
            System.out.printf("%2s%20s%40s%9.2f%8s%31s%31s\n", rate.getRentalRateId(), rate.getCarCategory().getCarCategory(), rate.getName(), rate.getRate(), rateType, start, end);
        });
    }
}
