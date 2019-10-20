package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
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

            Outlet outlet = new Outlet("somewhere", 8, 22);
            outlet.getEmployees().add(admin);
            outlet.getEmployees().add(sales);
            outlet.getEmployees().add(operations);
            outlet = outletSessionBeanLocal.createNewOutletWithEmployees(outlet);

            CarCategory luxurySedan = new CarCategory("Luxury Sedan");
            CarCategory familySedan = new CarCategory("Family Sedan");
            CarCategory standardSedan = new CarCategory("Standard Sedan");
            CarCategory suvMinivan = new CarCategory("SUV/Minivan");
            luxurySedan = carCategorySessionBeanLocal.createNewCarCategory(luxurySedan);
            familySedan = carCategorySessionBeanLocal.createNewCarCategory(familySedan);
            standardSedan = carCategorySessionBeanLocal.createNewCarCategory(standardSedan);
            suvMinivan = carCategorySessionBeanLocal.createNewCarCategory(suvMinivan);

            CarModel mercedesEClassModel = new CarModel("Mercedes", "E-class");
            CarModel toyotaCamryModel = new CarModel("Toyota", "Camry");
            CarModel mazda3Model = new CarModel("Mazda", "3");
            CarModel nissanXTrailModel = new CarModel("Nissan", "X-Trail");
            mercedesEClassModel = carModelSessionBeanLocal.createNewCarModel(mercedesEClassModel, luxurySedan);
            toyotaCamryModel = carModelSessionBeanLocal.createNewCarModel(toyotaCamryModel, familySedan);
            mazda3Model = carModelSessionBeanLocal.createNewCarModel(mazda3Model, standardSedan);
            nissanXTrailModel = carModelSessionBeanLocal.createNewCarModel(nissanXTrailModel, suvMinivan);

            Car eclass1 = new Car("SMA 123", "silver");
            Car eclass2 = new Car("SMA 456", "silver");
            Car eclass3 = new Car("SMA 789", "black");
            Car eclass4 = new Car("SMA 101112", "black");
            carSessionBeanLocal.createNewCar(eclass1, luxurySedan, mercedesEClassModel, outlet);
            carSessionBeanLocal.createNewCar(eclass2, luxurySedan, mercedesEClassModel, outlet);
            carSessionBeanLocal.createNewCar(eclass3, luxurySedan, mercedesEClassModel, outlet);
            carSessionBeanLocal.createNewCar(eclass4, luxurySedan, mercedesEClassModel, outlet);
           
            Car camry1 = new Car("SMB 123", "silver");
            Car camry2 = new Car("SMB 456", "silver");
            Car camry3 = new Car("SMB 789", "white");
            Car camry4 = new Car("SMB 101112", "white");
            carSessionBeanLocal.createNewCar(camry1, familySedan, toyotaCamryModel, outlet);
            carSessionBeanLocal.createNewCar(camry2, familySedan, toyotaCamryModel, outlet);
            carSessionBeanLocal.createNewCar(camry3, familySedan, toyotaCamryModel, outlet);
            carSessionBeanLocal.createNewCar(camry4, familySedan, toyotaCamryModel, outlet);
            
            Car mazda31 = new Car("SMC 123", "red");
            Car mazda32 = new Car("SMC 456", "red");
            Car mazda33 = new Car("SMC 789", "blue");
            Car mazda34 = new Car("SMC 101112", "blue");
            carSessionBeanLocal.createNewCar(mazda31, standardSedan, mazda3Model, outlet);
            carSessionBeanLocal.createNewCar(mazda32, standardSedan, mazda3Model, outlet);
            carSessionBeanLocal.createNewCar(mazda33, standardSedan, mazda3Model, outlet);
            carSessionBeanLocal.createNewCar(mazda34, standardSedan, mazda3Model, outlet);
            
            Car nissanXTrail1 = new Car("SMD 123", "orange");
            Car nissanXTrail2 = new Car("SMD 456", "white");
            Car nissanXTrail3 = new Car("SMD 789", "blue");
            Car nissanXTrail4 = new Car("SMD 101112", "silver");
            carSessionBeanLocal.createNewCar(nissanXTrail1, suvMinivan, nissanXTrailModel, outlet);
            carSessionBeanLocal.createNewCar(nissanXTrail2, suvMinivan, nissanXTrailModel, outlet);
            carSessionBeanLocal.createNewCar(nissanXTrail3, suvMinivan, nissanXTrailModel, outlet);
            carSessionBeanLocal.createNewCar(nissanXTrail4, suvMinivan, nissanXTrailModel, outlet);

        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }
}
