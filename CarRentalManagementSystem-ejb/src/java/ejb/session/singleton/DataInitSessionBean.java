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
            outletSessionBeanLocal.createNewOutletWithEmployees(outlet);

            CarCategory luxurySedan = new CarCategory("Luxury Sedan");
            CarCategory familySedan = new CarCategory("Family Sedan");
            CarCategory standardSedan = new CarCategory("Standard Sedan");
            CarCategory suvMinivan = new CarCategory("SUV/Minivan");
            luxurySedan = carCategorySessionBeanLocal.createNewCarCategory(luxurySedan);
            familySedan = carCategorySessionBeanLocal.createNewCarCategory(familySedan);
            standardSedan = carCategorySessionBeanLocal.createNewCarCategory(standardSedan);
            suvMinivan = carCategorySessionBeanLocal.createNewCarCategory(suvMinivan);

            CarModel mercedesEClass = new CarModel("Mercedes", "E-class");
            CarModel toyotaCamry = new CarModel("Toyota", "Camry");
            CarModel mazda3 = new CarModel("Mazda", "3");
            CarModel nissanXTrail = new CarModel("Nissan", "X-Trail");
            mercedesEClass = carModelSessionBeanLocal.createNewCarModel(mercedesEClass, luxurySedan);
            toyotaCamry = carModelSessionBeanLocal.createNewCarModel(toyotaCamry, familySedan);
            mazda3 = carModelSessionBeanLocal.createNewCarModel(mazda3, standardSedan);
            nissanXTrail = carModelSessionBeanLocal.createNewCarModel(nissanXTrail, suvMinivan);

            Car eclass1 = new Car("SMA 123", "silver");
            Car eclass2 = new Car("SMA 456", "silver");
            Car eclass3 = new Car("SMA 789", "black");
            Car eclass4 = new Car("SMA 101112", "black");
            carSessionBeanLocal.createNewCar(eclass1, luxurySedan, mercedesEClass);
            carSessionBeanLocal.createNewCar(eclass2, luxurySedan, mercedesEClass);
            carSessionBeanLocal.createNewCar(eclass3, luxurySedan, mercedesEClass);
            carSessionBeanLocal.createNewCar(eclass4, luxurySedan, mercedesEClass);

        } catch (UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }
}
