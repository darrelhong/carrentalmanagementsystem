package ejb.session.singleton;

import ejb.session.stateful.BookingSessionBeanLocal;
import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Employee;
import entity.Outlet;
import entity.RentalRate;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeType;
import util.exception.EmployeeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "BookingSessionBeanLocal")
    private BookingSessionBeanLocal bookingSessionBeanLocal;

    @EJB(name = "RentalRateSessionBeanLocal")
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @EJB(name = "CarSessionBeanLocal")
    private CarSessionBeanLocal carSessionBeanLocal;

    @EJB(name = "CarModelSessionBeanLocal")
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @EJB(name = "CarCategorySessionBeanLocal")
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("admin");
        } catch (EmployeeNotFoundException ex) {
            initialiseData();
        }
    }

    private void initialiseData() {
        try {
            Employee admin = new Employee("Default Admin", "admin", "password", EmployeeType.ADMIN);
            Employee sales = new Employee("Default Sales", "sales", "password", EmployeeType.SALES);
            Employee operations = new Employee("Default Operations", "operations", "password", EmployeeType.OPERATIONS);

            Outlet outlet1 = new Outlet("outlet1", 8, 22);
            Outlet outlet2 = new Outlet("outlet2", 8, 22);
            outlet1.getEmployees().add(admin);
            outlet1.getEmployees().add(sales);
            outlet1.getEmployees().add(operations);
            outlet1 = outletSessionBeanLocal.createNewOutletWithEmployees(outlet1);
            outlet2 = outletSessionBeanLocal.createNewOutletWithEmployees(outlet2);

            CarCategory luxurySedan = new CarCategory("Luxury Sedan");
            CarCategory familySedan = new CarCategory("Family Sedan");
            CarCategory standardSedan = new CarCategory("Standard Sedan");
            CarCategory suvMinivan = new CarCategory("SUV/Minivan");
            luxurySedan = carCategorySessionBeanLocal.createNewCarCategory(luxurySedan);
            familySedan = carCategorySessionBeanLocal.createNewCarCategory(familySedan);
            standardSedan = carCategorySessionBeanLocal.createNewCarCategory(standardSedan);
            suvMinivan = carCategorySessionBeanLocal.createNewCarCategory(suvMinivan);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
            Date firstJan = sdf.parse("1/1/2019 00:00 am");
            Date secondJan = sdf.parse("2/1/2019 11:59 pm");
            Date thirdJan = sdf.parse("3/1/2019 00:00 am");
            Date fourthJan = sdf.parse("4/1/2019 11:59 pm");
            RentalRate luxFirst = new RentalRate("LuxuryFirst", new BigDecimal(45), false, firstJan, secondJan);
            RentalRate luxSecond = new RentalRate("LuxurySecond", new BigDecimal(55), false, thirdJan, fourthJan);
            rentalRateSessionBeanLocal.createRentalRate(luxFirst, luxurySedan);
            rentalRateSessionBeanLocal.createRentalRate(luxSecond, luxurySedan);
            Date startDate = new Date(119, 0, 1, 0, 0);
            Date endDate = new Date(120, 0, 1, 23, 59);
            RentalRate luxurySedanPeakRate = new RentalRate("Luxury Sedan Normal Rate", new BigDecimal(250), false, startDate, endDate);
            RentalRate luxurySedanNonPeakRate = new RentalRate("Luxury Sedan Promo Rate", new BigDecimal(200), false, startDate, endDate);
            RentalRate familySedanPeakRate = new RentalRate("Family Sedan Normal Rate", new BigDecimal(180), false, startDate, endDate);
            RentalRate familySedanNonPeakRate = new RentalRate("Family Sedan Promo Rate", new BigDecimal(150), false, startDate, endDate);
            RentalRate standardSedanPeakRate = new RentalRate("Standard Sedan Normal Rate", new BigDecimal(120), false, startDate, endDate);
            RentalRate standardSedanNonPeakRate = new RentalRate("Standard Sedan Promo Rate", new BigDecimal(100), false, startDate, endDate);
            RentalRate suvMinivanPeakRate = new RentalRate("SUV/Minivan Normal Rate", new BigDecimal(190), false, startDate, endDate);
            RentalRate suvMinivanNonPeakRate = new RentalRate("SUV/Minivan Promo Rate", new BigDecimal(170), false, startDate, endDate);

            rentalRateSessionBeanLocal.createRentalRate(luxurySedanPeakRate, luxurySedan);
            rentalRateSessionBeanLocal.createRentalRate(luxurySedanNonPeakRate, luxurySedan);
            rentalRateSessionBeanLocal.createRentalRate(familySedanPeakRate, familySedan);
            rentalRateSessionBeanLocal.createRentalRate(familySedanNonPeakRate, familySedan);
            rentalRateSessionBeanLocal.createRentalRate(standardSedanPeakRate, standardSedan);
            rentalRateSessionBeanLocal.createRentalRate(standardSedanNonPeakRate, standardSedan);
            rentalRateSessionBeanLocal.createRentalRate(suvMinivanPeakRate, suvMinivan);
            rentalRateSessionBeanLocal.createRentalRate(suvMinivanNonPeakRate, suvMinivan);

            CarModel mercedesEClassModel = new CarModel("Mercedes", "E-class");
            CarModel toyotaCamryModel = new CarModel("Toyota", "Camry");
            CarModel mazda3Model = new CarModel("Mazda", "3");
            CarModel nissanXTrailModel = new CarModel("Nissan", "X-Trail");
            mercedesEClassModel = carModelSessionBeanLocal.createNewCarModel(mercedesEClassModel, luxurySedan);
            toyotaCamryModel = carModelSessionBeanLocal.createNewCarModel(toyotaCamryModel, familySedan);
            mazda3Model = carModelSessionBeanLocal.createNewCarModel(mazda3Model, standardSedan);
            nissanXTrailModel = carModelSessionBeanLocal.createNewCarModel(nissanXTrailModel, suvMinivan);

            CarModel bmw5SeriesModel = new CarModel("BMW", "5 Series");
            CarModel hondaAccordModel = new CarModel("Honda", "Accord");
            CarModel toyotaCorollaModel = new CarModel("Toyata", "Corolla");
            CarModel toyotaRav4Model = new CarModel("Toyota", "RAV4");
            bmw5SeriesModel = carModelSessionBeanLocal.createNewCarModel(bmw5SeriesModel, luxurySedan);
            hondaAccordModel = carModelSessionBeanLocal.createNewCarModel(hondaAccordModel, familySedan);
            toyotaCorollaModel = carModelSessionBeanLocal.createNewCarModel(toyotaCorollaModel, standardSedan);
            toyotaRav4Model = carModelSessionBeanLocal.createNewCarModel(toyotaRav4Model, suvMinivan);

            Car eclass1 = new Car("SMA 123", "silver");
            Car eclass2 = new Car("SMA 456", "silver");
//            Car eclass3 = new Car("SMA 789", "black");
//            Car eclass4 = new Car("SMA 101112", "black");
            Car bmw5Series1 = new Car("SME 123", "white");
            Car bmw5Series2 = new Car("SME 456", "grey");
            carSessionBeanLocal.createNewCar(eclass1, luxurySedan, mercedesEClassModel, outlet1);
            carSessionBeanLocal.createNewCar(eclass2, luxurySedan, mercedesEClassModel, outlet2);
//            carSessionBeanLocal.createNewCar(eclass3, luxurySedan, mercedesEClassModel, outlet);
//            carSessionBeanLocal.createNewCar(eclass4, luxurySedan, mercedesEClassModel, outlet);
            carSessionBeanLocal.createNewCar(bmw5Series1, luxurySedan, bmw5SeriesModel, outlet1);
            carSessionBeanLocal.createNewCar(bmw5Series2, luxurySedan, bmw5SeriesModel, outlet2);

            Car camry1 = new Car("SMB 123", "silver");
            Car camry2 = new Car("SMB 456", "silver");
//            Car camry3 = new Car("SMB 789", "white");
//            Car camry4 = new Car("SMB 101112", "white");
            Car accord1 = new Car("SMF 123", "grey");
            Car accord2 = new Car("SMF 456", "grey");
            carSessionBeanLocal.createNewCar(camry1, familySedan, toyotaCamryModel, outlet1);
            carSessionBeanLocal.createNewCar(camry2, familySedan, toyotaCamryModel, outlet2);
//            carSessionBeanLocal.createNewCar(camry3, familySedan, toyotaCamryModel, outlet);
//            carSessionBeanLocal.createNewCar(camry4, familySedan, toyotaCamryModel, outlet);
            carSessionBeanLocal.createNewCar(accord1, familySedan, hondaAccordModel, outlet1);
            carSessionBeanLocal.createNewCar(accord2, familySedan, hondaAccordModel, outlet2);

            Car mazda31 = new Car("SMC 123", "red");
            Car mazda32 = new Car("SMC 456", "red");
//            Car mazda33 = new Car("SMC 789", "blue");
//            Car mazda34 = new Car("SMC 101112", "blue");
            Car corolla1 = new Car("SMG 123", "brown");
            Car corolla2 = new Car("SMG 456", "white");
            carSessionBeanLocal.createNewCar(mazda31, standardSedan, mazda3Model, outlet1);
            carSessionBeanLocal.createNewCar(mazda32, standardSedan, mazda3Model, outlet2);
//            carSessionBeanLocal.createNewCar(mazda33, standardSedan, mazda3Model, outlet);
//            carSessionBeanLocal.createNewCar(mazda34, standardSedan, mazda3Model, outlet);
            carSessionBeanLocal.createNewCar(corolla1, standardSedan, toyotaCorollaModel, outlet1);
            carSessionBeanLocal.createNewCar(corolla2, standardSedan, toyotaCorollaModel, outlet2);

            Car nissanXTrail1 = new Car("SMD 123", "orange");
            Car nissanXTrail2 = new Car("SMD 456", "white");
//            Car nissanXTrail3 = new Car("SMD 789", "blue");
//            Car nissanXTrail4 = new Car("SMD 101112", "silver");
            Car rav4_1 = new Car("SMH 123", "black");
            Car rav4_2 = new Car("SMH 456", "white");
            carSessionBeanLocal.createNewCar(nissanXTrail1, suvMinivan, nissanXTrailModel, outlet1);
            carSessionBeanLocal.createNewCar(nissanXTrail2, suvMinivan, nissanXTrailModel, outlet2);
//            carSessionBeanLocal.createNewCar(nissanXTrail3, suvMinivan, nissanXTrailModel, outlet);
//            carSessionBeanLocal.createNewCar(nissanXTrail4, suvMinivan, nissanXTrailModel, outlet);
            carSessionBeanLocal.createNewCar(rav4_1, suvMinivan, toyotaRav4Model, outlet1);
            carSessionBeanLocal.createNewCar(rav4_2, suvMinivan, toyotaRav4Model, outlet2);

            Customer customer = new Customer();
            customer.setName("name");
            customer.setEmail("email");
            customer.setPassword("password");
            customerSessionBeanLocal.createNewCustomer(customer);
            RentalRecord rr1 = new RentalRecord(new Date(119, 9, 20), new Date(119, 9, 30), "123123123", new BigDecimal(411), true, false, false, false, outlet1, outlet1, customer, mercedesEClassModel);
            RentalRecord rr2 = new RentalRecord(new Date(119, 9, 20), new Date(119, 9, 30), "123123123", new BigDecimal(411), true, false, false, false, outlet1, outlet1, customer, luxurySedan);
            bookingSessionBeanLocal.createTestRentalRecord(rr1);
            bookingSessionBeanLocal.createTestRentalRecord(rr2);

        } catch (UnknownPersistenceException | ParseException ex) {
            ex.printStackTrace();
        }
    }
}
