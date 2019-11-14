package ejb.session.singleton;

import ejb.session.stateful.BookingSessionBeanLocal;
import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.RentalRecordSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
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
import util.enumeration.PartnerType;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author 
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "PartnerSessionBeanLocal")
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB(name = "RentalRecordSessionBeanLocal")
    private RentalRecordSessionBeanLocal rentalRecordSessionBeanLocal;

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
            outletSessionBeanLocal.retrieveOutletByOutletId(1L);
        } catch (OutletNotFoundException ex) {
            initialiseData();
        }
    }

//    @PostConstruct
//    public void postConstruct() {
//        try {
//            employeeSessionBeanLocal.retrieveEmployeeByUsername("admin");
//        } catch (EmployeeNotFoundException ex) {
//            initialiseData();
//        }
//    }
    private void initialiseData() {
        try {
            Outlet outletA = new Outlet("Outlet A", null, null);
            Outlet outletB = new Outlet("Outlet B", null, null);
            Outlet outletC = new Outlet("Outlet C", 10, 22);

            Employee a1 = new Employee("Sales Manager A", "salesA", "password", EmployeeType.SALES);
            Employee a2 = new Employee("Operations Manager A", "operationsA", "password", EmployeeType.OPERATIONS);
            Employee a3 = new Employee("Cust Svc Exec A", "custsvcA", "password", EmployeeType.CUSTOMERSERVICE);
            Employee a4 = new Employee("Employee 1 A", "employee1A", "password", EmployeeType.EMPLOYEE);
            Employee a5 = new Employee("Employee 2 A", "employee2A", "password", EmployeeType.EMPLOYEE);
            Employee a6 = new Employee("Admin A", "adminA", "password", EmployeeType.ADMIN);

            Employee b1 = new Employee("Sales Manager B", "salesB", "password", EmployeeType.SALES);
            Employee b2 = new Employee("Operations Manager B", "operationsB", "password", EmployeeType.OPERATIONS);
            Employee b3 = new Employee("Cust Svc Exec B", "custsvcB", "password", EmployeeType.CUSTOMERSERVICE);

            Employee c1 = new Employee("Sales Manager C", "salesC", "password", EmployeeType.SALES);
            Employee c2 = new Employee("Operations Manager C", "operationsC", "password", EmployeeType.OPERATIONS);
            Employee c3 = new Employee("Cust Svc Exec C", "custsvcC", "password", EmployeeType.CUSTOMERSERVICE);

            outletA.getEmployees().add(a1);
            outletA.getEmployees().add(a2);
            outletA.getEmployees().add(a3);
            outletA.getEmployees().add(a4);
            outletA.getEmployees().add(a5);
            outletA.getEmployees().add(a6);
            outletB.getEmployees().add(b1);
            outletB.getEmployees().add(b2);
            outletB.getEmployees().add(b3);
            outletC.getEmployees().add(c1);
            outletC.getEmployees().add(c2);
            outletC.getEmployees().add(c3);

            outletA = outletSessionBeanLocal.createNewOutletWithEmployees(outletA);
            outletB = outletSessionBeanLocal.createNewOutletWithEmployees(outletB);
            outletC = outletSessionBeanLocal.createNewOutletWithEmployees(outletC);
            CarCategory standardSedan = new CarCategory("Standard Sedan");
            CarCategory familySedan = new CarCategory("Family Sedan");
            CarCategory luxurySedan = new CarCategory("Luxury Sedan");
            CarCategory suvMinivan = new CarCategory("SUV/Minivan");
            standardSedan = carCategorySessionBeanLocal.createNewCarCategory(standardSedan);
            familySedan = carCategorySessionBeanLocal.createNewCarCategory(familySedan);
            luxurySedan = carCategorySessionBeanLocal.createNewCarCategory(luxurySedan);
            suvMinivan = carCategorySessionBeanLocal.createNewCarCategory(suvMinivan);

            CarModel corollaModel = new CarModel("Toyota", "Corolla");
            CarModel civicModel = new CarModel("Honda", "Civic");
            CarModel sunnyModel = new CarModel("Nissan", "Sunny");
            CarModel eclassModel = new CarModel("Merceddes", "E-Class");
            CarModel bmw5seriesModel = new CarModel("BMW", "5 Series");
            CarModel a6Model = new CarModel("Audi", "A6");

            
            corollaModel = carModelSessionBeanLocal.createNewCarModel(corollaModel, standardSedan);
            civicModel = carModelSessionBeanLocal.createNewCarModel(civicModel, standardSedan);
            sunnyModel = carModelSessionBeanLocal.createNewCarModel(sunnyModel, standardSedan);
            eclassModel = carModelSessionBeanLocal.createNewCarModel(eclassModel, luxurySedan);
            bmw5seriesModel = carModelSessionBeanLocal.createNewCarModel(bmw5seriesModel, luxurySedan);
            a6Model = carModelSessionBeanLocal.createNewCarModel(a6Model, luxurySedan);

            Car corollaA1 = new Car("SS00A1TC", "silver");
            Car corollaA2 = new Car("SS00A2TC", "brown");
            Car corollaA3 = new Car("SS00A3TC", "blue");
            carSessionBeanLocal.createNewCar(corollaA1, standardSedan, corollaModel, outletA);
            carSessionBeanLocal.createNewCar(corollaA2, standardSedan, corollaModel, outletA);
            carSessionBeanLocal.createNewCar(corollaA3, standardSedan, corollaModel, outletA);
            Car civicB1 = new Car("SS00B1HC", "grey");
            Car civicB2 = new Car("SS00B2HC", "blue");
            Car civicB3 = new Car("SS00B3HC", "red");
            carSessionBeanLocal.createNewCar(civicB1, standardSedan, civicModel, outletB);
            carSessionBeanLocal.createNewCar(civicB2, standardSedan, civicModel, outletB);
            carSessionBeanLocal.createNewCar(civicB2, standardSedan, civicModel, outletB);
            Car sunnyC1 = new Car("SS00C1NS", "gold");
            Car sunnyC2 = new Car("SS00C2NS", "blue");
            Car sunnyC3 = new Car("SS00C3NS", "silver");
            carSessionBeanLocal.createNewCar(sunnyC1, standardSedan, sunnyModel, outletC);
            carSessionBeanLocal.createNewCar(sunnyC2, standardSedan, sunnyModel, outletC);
            carSessionBeanLocal.createNewCar(sunnyC3, standardSedan, sunnyModel, outletC);
            Car eclassA1 = new Car("LS00A4ME", "black");
            Car bmw5SeriesB1 = new Car("LS00B4B5", "navy");
            Car audiA6C1 = new Car("LS00C4A6", "white");
            carSessionBeanLocal.createNewCar(eclassA1, luxurySedan, eclassModel, outletA);
            carSessionBeanLocal.createNewCar(bmw5SeriesB1, luxurySedan, bmw5seriesModel, outletB);
            carSessionBeanLocal.createNewCar(audiA6C1, luxurySedan, a6Model, outletC);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
            RentalRate defaultSedan = new RentalRate("Standard Sedan Default", new BigDecimal(100), false, null, null);
            RentalRate weekendPromoSedan = new RentalRate("Standard Sedan Weekend Promo", new BigDecimal(80), true, sdf.parse("06/12/2019 12:00 PM"), sdf.parse("08/12/2019 12:00 AM"));
            RentalRate defaultFamily = new RentalRate("Family Sedan Default", new BigDecimal(200), false, null, null);
            RentalRate luxuryMon = new RentalRate("Luxury Sedan Monday", new BigDecimal(310), false, sdf.parse("02/12/2019 12:00 AM"), sdf.parse("02/12/2019 11:59 PM"));
            RentalRate luxuryTues = new RentalRate("Luxury Sedan Tuesday", new BigDecimal(320), false, sdf.parse("03/12/2019 12:00 AM"), sdf.parse("03/12/2019 11:59 PM"));
            RentalRate luxuryWed = new RentalRate("Luxury Sedan Wednesday", new BigDecimal(330), false, sdf.parse("04/12/2019 12:00 AM"), sdf.parse("04/12/2019 11:59 PM"));
            RentalRate luxuryWeekdayPromo = new RentalRate("Luxury Sedan Weekday Promo", new BigDecimal(250), true, sdf.parse("04/12/2019 12:00 AM"), sdf.parse("05/12/2019 12:00 AM"));
            rentalRateSessionBeanLocal.createRentalRate(defaultSedan, standardSedan);
            rentalRateSessionBeanLocal.createRentalRate(weekendPromoSedan, standardSedan);
            rentalRateSessionBeanLocal.createRentalRate(defaultFamily, familySedan);
            rentalRateSessionBeanLocal.createRentalRate(luxuryMon, luxurySedan);
            rentalRateSessionBeanLocal.createRentalRate(luxuryTues, luxurySedan);
            rentalRateSessionBeanLocal.createRentalRate(luxuryWed, luxurySedan);
            rentalRateSessionBeanLocal.createRentalRate(luxuryWeekdayPromo, luxurySedan);

            Partner partnerManager = new Partner("Holiday.com Partner Manager", "partner", "password", PartnerType.MANAGER);
            Partner partnerEmployee = new Partner("Holiday.com Partner Employee", "employee", "password", PartnerType.EMPLOYEE);
            partnerManager = partnerSessionBeanLocal.createNewPartner(partnerManager);
            partnerEmployee = partnerSessionBeanLocal.createNewPartner(partnerEmployee);
            

//            Employee admin = new Employee("Default Admin", "admin", "password", EmployeeType.ADMIN);
//            Employee sales = new Employee("Default Sales", "sales", "password", EmployeeType.SALES);
//            Employee operations = new Employee("Default Operations", "operations", "password", EmployeeType.OPERATIONS);
//            Employee custSvc = new Employee("Default Cust Svc", "custsvc", "password", EmployeeType.CUSTOMERSERVICE);
//
//            Employee admin2 = new Employee("Default Admin 2", "admin2", "password", EmployeeType.ADMIN);
//            Employee sales2 = new Employee("Default Sales 2", "sales2", "password", EmployeeType.SALES);
//            Employee operations2 = new Employee("Default Operations 2", "operations2", "password", EmployeeType.OPERATIONS);
//            Employee custSvc2 = new Employee("Default Cust Svc 2", "custsvc2", "password", EmployeeType.CUSTOMERSERVICE);
//
//            Outlet outlet1 = new Outlet("outlet1", 8, 22);
//            Outlet outlet2 = new Outlet("outlet2", 8, 22);
//            outlet1.getEmployees().add(admin);
//            outlet1.getEmployees().add(sales);
//            outlet1.getEmployees().add(operations);
//            outlet1.getEmployees().add(custSvc);
//            outlet1 = outletSessionBeanLocal.createNewOutletWithEmployees(outlet1);
//            outlet2.getEmployees().add(admin2);
//            outlet2.getEmployees().add(sales2);
//            outlet2.getEmployees().add(operations2);
//            outlet2.getEmployees().add(custSvc2);
//            outlet2 = outletSessionBeanLocal.createNewOutletWithEmployees(outlet2);
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy hh:mm a");
//            Date firstJan = sdf.parse("1/1/2019 00:00 am");
//            Date secondJan = sdf.parse("2/1/2019 11:59 pm");
//            Date thirdJan = sdf.parse("3/1/2019 00:00 am");
//            Date fourthJan = sdf.parse("4/1/2019 11:59 pm");
//            RentalRate luxFirst = new RentalRate("LuxuryFirst", new BigDecimal(45), false, firstJan, secondJan);
//            RentalRate luxSecond = new RentalRate("LuxurySecond", new BigDecimal(55), false, thirdJan, fourthJan);
//            rentalRateSessionBeanLocal.createRentalRate(luxFirst, luxurySedan);
//            rentalRateSessionBeanLocal.createRentalRate(luxSecond, luxurySedan);
//            Date startDate = new Date(119, 0, 1, 0, 0);
//            Date endDate = new Date(120, 0, 1, 23, 59);
//            RentalRate luxurySedanPeakRate = new RentalRate("Luxury Sedan Normal Rate", new BigDecimal(250), false, startDate, endDate);
//            RentalRate luxurySedanNonPeakRate = new RentalRate("Luxury Sedan Promo Rate", new BigDecimal(200), false, startDate, endDate);
//            RentalRate familySedanPeakRate = new RentalRate("Family Sedan Normal Rate", new BigDecimal(180), false, startDate, endDate);
//            RentalRate familySedanNonPeakRate = new RentalRate("Family Sedan Promo Rate", new BigDecimal(150), false, startDate, endDate);
//            RentalRate standardSedanPeakRate = new RentalRate("Standard Sedan Normal Rate", new BigDecimal(120), false, startDate, endDate);
//            RentalRate standardSedanNonPeakRate = new RentalRate("Standard Sedan Promo Rate", new BigDecimal(100), false, startDate, endDate);
//            RentalRate suvMinivanPeakRate = new RentalRate("SUV/Minivan Normal Rate", new BigDecimal(190), false, startDate, endDate);
//            RentalRate suvMinivanNonPeakRate = new RentalRate("SUV/Minivan Promo Rate", new BigDecimal(170), false, startDate, endDate);
//
//            rentalRateSessionBeanLocal.createRentalRate(luxurySedanPeakRate, luxurySedan);
//            rentalRateSessionBeanLocal.createRentalRate(luxurySedanNonPeakRate, luxurySedan);
//            rentalRateSessionBeanLocal.createRentalRate(familySedanPeakRate, familySedan);
//            rentalRateSessionBeanLocal.createRentalRate(familySedanNonPeakRate, familySedan);
//            rentalRateSessionBeanLocal.createRentalRate(standardSedanPeakRate, standardSedan);
//            rentalRateSessionBeanLocal.createRentalRate(standardSedanNonPeakRate, standardSedan);
//            rentalRateSessionBeanLocal.createRentalRate(suvMinivanPeakRate, suvMinivan);
//            rentalRateSessionBeanLocal.createRentalRate(suvMinivanNonPeakRate, suvMinivan);
//            CarModel mercedesEClassModel = new CarModel("Mercedes", "E-class");
//            CarModel toyotaCamryModel = new CarModel("Toyota", "Camry");
//            CarModel mazda3Model = new CarModel("Mazda", "3");
//            CarModel nissanXTrailModel = new CarModel("Nissan", "X-Trail");
//            mercedesEClassModel = carModelSessionBeanLocal.createNewCarModel(mercedesEClassModel, luxurySedan);
//            toyotaCamryModel = carModelSessionBeanLocal.createNewCarModel(toyotaCamryModel, familySedan);
//            mazda3Model = carModelSessionBeanLocal.createNewCarModel(mazda3Model, standardSedan);
//            nissanXTrailModel = carModelSessionBeanLocal.createNewCarModel(nissanXTrailModel, suvMinivan); 
//            CarModel bmw5SeriesModel = new CarModel("BMW", "5 Series");
//            CarModel hondaAccordModel = new CarModel("Honda", "Accord");
//            CarModel toyotaCorollaModel = new CarModel("Toyata", "Corolla");
//            CarModel toyotaRav4Model = new CarModel("Toyota", "RAV4");
//            bmw5SeriesModel = carModelSessionBeanLocal.createNewCarModel(bmw5SeriesModel, luxurySedan);
//            hondaAccordModel = carModelSessionBeanLocal.createNewCarModel(hondaAccordModel, familySedan);
//            toyotaCorollaModel = carModelSessionBeanLocal.createNewCarModel(toyotaCorollaModel, standardSedan);
//            toyotaRav4Model = carModelSessionBeanLocal.createNewCarModel(toyotaRav4Model, suvMinivan);
//            Car eclass1 = new Car("SMA 123", "silver");
//            Car eclass2 = new Car("SMA 456", "silver");
////            Car eclass3 = new Car("SMA 789", "black");
////            Car eclass4 = new Car("SMA 101112", "black");
//            Car bmw5Series1 = new Car("SME 123", "white");
////            Car bmw5Series2 = new Car("SME 456", "grey");
//            carSessionBeanLocal.createNewCar(eclass1, luxurySedan, mercedesEClassModel, outlet1);
//            carSessionBeanLocal.createNewCar(eclass2, luxurySedan, mercedesEClassModel, outlet2);
////            carSessionBeanLocal.createNewCar(eclass3, luxurySedan, mercedesEClassModel, outlet);
////            carSessionBeanLocal.createNewCar(eclass4, luxurySedan, mercedesEClassModel, outlet);
//            carSessionBeanLocal.createNewCar(bmw5Series1, luxurySedan, bmw5SeriesModel, outlet1);
////            carSessionBeanLocal.createNewCar(bmw5Series2, luxurySedan, bmw5SeriesModel, outlet2);
//
//            Car camry1 = new Car("SMB 123", "silver");
////            Car camry2 = new Car("SMB 456", "silver");
////            Car camry3 = new Car("SMB 789", "white");
////            Car camry4 = new Car("SMB 101112", "white");
//            Car accord1 = new Car("SMF 123", "grey");
////            Car accord2 = new Car("SMF 456", "grey");
//            carSessionBeanLocal.createNewCar(camry1, familySedan, toyotaCamryModel, outlet1);
////            carSessionBeanLocal.createNewCar(camry2, familySedan, toyotaCamryModel, outlet2);
////            carSessionBeanLocal.createNewCar(camry3, familySedan, toyotaCamryModel, outlet);
////            carSessionBeanLocal.createNewCar(camry4, familySedan, toyotaCamryModel, outlet);
//            carSessionBeanLocal.createNewCar(accord1, familySedan, hondaAccordModel, outlet1);
////            carSessionBeanLocal.createNewCar(accord2, familySedan, hondaAccordModel, outlet2);
//
//            Car mazda31 = new Car("SMC 123", "red");
////            Car mazda32 = new Car("SMC 456", "red");
////            Car mazda33 = new Car("SMC 789", "blue");
////            Car mazda34 = new Car("SMC 101112", "blue");
//            Car corolla1 = new Car("SMG 123", "brown");
////            Car corolla2 = new Car("SMG 456", "white");
//            carSessionBeanLocal.createNewCar(mazda31, standardSedan, mazda3Model, outlet1);
////            carSessionBeanLocal.createNewCar(mazda32, standardSedan, mazda3Model, outlet2);
////            carSessionBeanLocal.createNewCar(mazda33, standardSedan, mazda3Model, outlet);
////            carSessionBeanLocal.createNewCar(mazda34, standardSedan, mazda3Model, outlet);
//            carSessionBeanLocal.createNewCar(corolla1, standardSedan, toyotaCorollaModel, outlet1);
////            carSessionBeanLocal.createNewCar(corolla2, standardSedan, toyotaCorollaModel, outlet2);
//
//            Car nissanXTrail1 = new Car("SMD 123", "orange");
////            Car nissanXTrail2 = new Car("SMD 456", "white");
////            Car nissanXTrail3 = new Car("SMD 789", "blue");
////            Car nissanXTrail4 = new Car("SMD 101112", "silver");
////            Car rav4_1 = new Car("SMH 123", "black");
//            Car rav4_2 = new Car("SMH 456", "white");
//            carSessionBeanLocal.createNewCar(nissanXTrail1, suvMinivan, nissanXTrailModel, outlet1);
////            carSessionBeanLocal.createNewCar(nissanXTrail2, suvMinivan, nissanXTrailModel, outlet2);
////            carSessionBeanLocal.createNewCar(nissanXTrail3, suvMinivan, nissanXTrailModel, outlet);
////            carSessionBeanLocal.createNewCar(nissanXTrail4, suvMinivan, nissanXTrailModel, outlet);
////            carSessionBeanLocal.createNewCar(rav4_1, suvMinivan, toyotaRav4Model, outlet1);
//            carSessionBeanLocal.createNewCar(rav4_2, suvMinivan, toyotaRav4Model, outlet2);
//            Partner partnerManager = new Partner("Default Partner Manager", "partner", "password", PartnerType.MANAGER);
//            Partner partnerEmployee = new Partner("Default Partner Employee", "employee", "password", PartnerType.EMPLOYEE);
//            partnerManager = partnerSessionBeanLocal.createNewPartner(partnerManager);
//            partnerEmployee = partnerSessionBeanLocal.createNewPartner(partnerEmployee);

//            Customer customer = new Customer();
//            customer.setName("name");
//            customer.setEmail("email@gmail.com");
//            customer.setPassword("password");
//            customerSessionBeanLocal.createNewCustomer(customer);
//            RentalRecord rr1 = new RentalRecord(new Date(119, 9, 20), new Date(119, 9, 30), "123123123", new BigDecimal(411), true, false, false, false, outlet1, outlet1, customer, mercedesEClassModel);
//            RentalRecord rr2 = new RentalRecord(new Date(119, 9, 20), new Date(119, 9, 30), "123123123", new BigDecimal(411), true, false, false, false, outlet1, outlet1, customer, luxurySedan);
//            RentalRecord rr3 = new RentalRecord(sdf.parse("1/1/2019 12:00 PM"), sdf.parse("1/1/2019 2:00 PM"), "1231231", new BigDecimal(999), true, false, false, false, outlet1, outlet1, customer, mercedesEClassModel);
//            RentalRecord rr4 = new RentalRecord(sdf.parse("1/1/2019 12:00 PM"), sdf.parse("1/1/2019 2:00 PM"), "1231231", new BigDecimal(999), true, false, false, false, outlet1, outlet1, customer, mercedesEClassModel);
//            RentalRecord rr5 = new RentalRecord(sdf.parse("1/1/2019 12:00 PM"), sdf.parse("1/1/2019 2:00 PM"), "1231231", new BigDecimal(999), true, false, false, false, outlet1, outlet1, customer, suvMinivan);
//            RentalRecord rr6 = new RentalRecord(sdf.parse("1/1/2019 12:00 PM"), sdf.parse("1/1/2019 2:00 PM"), "1231231", new BigDecimal(999), true, false, false, false, outlet1, outlet1, customer, suvMinivan);

//            bookingSessionBeanLocal.createTestRentalRecord(rr1);
//            bookingSessionBeanLocal.createTestRentalRecord(rr2);
//            bookingSessionBeanLocal.createTestRentalRecord(rr3);
//            bookingSessionBeanLocal.createTestRentalRecord(rr4);
//            bookingSessionBeanLocal.createTestRentalRecord(rr5);
//            bookingSessionBeanLocal.createTestRentalRecord(rr6);
//            rentalRecordSessionBeanLocal.allocateCars();
//            rentalRecordSessionBeanLocal.retrieveTransitDispatchRecords();
        } catch (UnknownPersistenceException | ParseException | InputDataValidationException ex) {
            ex.printStackTrace();
        }
    }
}
