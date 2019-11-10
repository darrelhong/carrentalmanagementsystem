package ejb.session.ws;

import ejb.session.stateful.BookingSessionBeanLocal;
import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRecordSessionBeanLocal;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import entity.Partner;
import entity.RentalRecord;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CancellationErrorException;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.InsufficientInventoryException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.NoRateFoundException;
import util.exception.OutletClosedException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.RentalRecordNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author darre
 */
@WebService(serviceName = "CaRMSWebService")
@Stateless()
public class CaRMSWebService {
    
    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;
    
    @EJB(name = "CarModelSessionBeanLocal")
    private CarModelSessionBeanLocal carModelSessionBeanLocal;
    
    @EJB(name = "CarCategorySessionBeanLocal")
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;
    
    @EJB(name = "BookingSessionBeanLocal")
    private BookingSessionBeanLocal bookingSessionBeanLocal;
    
    @EJB(name = "RentalRecordSessionBeanLocal")
    private RentalRecordSessionBeanLocal rentalRecordSessionBeanLocal;
    
    @EJB(name = "PartnerSessionBeanLocal")
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "login")
    public Partner login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialsException {
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
        return partner;
    }
    
    @WebMethod(operationName = "retrieveAllPartnerReservations")
    public List<RentalRecord> retrieveAllPartnerReservations(@WebParam(name = "username") String username) throws PartnerNotFoundException {
        return rentalRecordSessionBeanLocal.retrieveAllPartnerReservations(username);
    }
    
    @WebMethod(operationName = "viewReservationDetails")
    public RentalRecord viewReservationDetails(@WebParam(name = "bookingId") Long bookingId) throws RentalRecordNotFoundException {
        return rentalRecordSessionBeanLocal.retrieveRentalRecordById(bookingId);
    }
    
    @WebMethod(operationName = "searchByCarModel")
    public BigDecimal searchByCarModel(@WebParam(name = "categoryId") Long categoryId, @WebParam(name = "modelId") Long modelId,
            @WebParam(name = "start") Date start, @WebParam(name = "end") Date end, @WebParam(name = "pickupOutletId") Long pickupOutletId,
            @WebParam(name = "returnOutletId") Long returnOutletId) throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, OutletClosedException {
        return bookingSessionBeanLocal.searchByCarModel(categoryId, modelId, start, end, pickupOutletId, returnOutletId);
    }
    
    @WebMethod(operationName = "searchByCarCategory")
    public BigDecimal searchByCarCategory(@WebParam(name = "categoryId") Long categoryId,
            @WebParam(name = "start") Date start, @WebParam(name = "end") Date end, @WebParam(name = "pickupOutletId") Long pickupOutletId,
            @WebParam(name = "returnOutletId") Long returnOutletId) throws CarCategoryNotFoundException, OutletNotFoundException, NoRateFoundException, InsufficientInventoryException, OutletClosedException {
        return bookingSessionBeanLocal.searchByCarCategory(categoryId, start, end, pickupOutletId, returnOutletId);
    }
    
    @WebMethod(operationName = "retrieveAllCarCategories")
    public List<CarCategory> retrieveAllCarCategories() {
        return carCategorySessionBeanLocal.retrieveAllCarCategories();
    }
    
    @WebMethod(operationName = "retrieveCarModelsByCategory")
    public List<CarModel> retrieveCarModelsByCategory(@WebParam(name = "categoryId") Long categoryId) {
        return carModelSessionBeanLocal.retrieveCarModelsByCategory(categoryId);
    }
    
    @WebMethod(operationName = "retrieveAllOutlets")
    public List<Outlet> retrieveAllOutlets() {
        return outletSessionBeanLocal.retrieveAllOutlets();
    }
    
    @WebMethod(operationName = "cancelReservation")
    public String cancelReservation(@WebParam(name = "recordId") Long recordId) throws CancellationErrorException {
        return rentalRecordSessionBeanLocal.cancelReservation(recordId);
    }

    @WebMethod(operationName = "confirmReservation")
    public void confirmReservation(@WebParam(name = "partnerId") Long partnerId, @WebParam(name = "ccNum") String ccNum, @WebParam(name = "immediatePayment") Boolean immediatePayment, @WebParam(name = "categoryId") Long categoryId, @WebParam(name = "modelId") Long modelId, @WebParam(name = "start") Date start, @WebParam(name = "end") Date end, @WebParam(name = "pickupOutletId") Long pickupOutletId, @WebParam(name = "returnOutletId") Long returnOutletId, @WebParam(name = "totalRate") BigDecimal totalRate, @WebParam(name = "externalCustName") String externalCustName) throws UnknownPersistenceException {
        bookingSessionBeanLocal.confirmReservation(partnerId, ccNum, immediatePayment, categoryId, modelId, start, end, pickupOutletId, returnOutletId, totalRate, externalCustName);
    }
}
