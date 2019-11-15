package holidayreservationsystem;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import util.helper.Print;
import ws.client.CancellationErrorException_Exception;
import ws.client.CarCategory;
import ws.client.CarCategoryNotFoundException_Exception;
import ws.client.CarModel;
import ws.client.CarModelNotFoundException_Exception;
import ws.client.InsufficientInventoryException_Exception;
import ws.client.InvalidLoginCredentialsException_Exception;
import ws.client.NoRateFoundException_Exception;
import ws.client.Outlet;
import ws.client.OutletClosedException_Exception;
import ws.client.OutletNotFoundException_Exception;
import ws.client.Partner;
import ws.client.PartnerNotFoundException_Exception;
import ws.client.RentalRecord;
import ws.client.RentalRecordNotFoundException_Exception;
import ws.client.UnknownPersistenceException_Exception;

public class MainApp {

    Partner currentPartner;

    Long categoryId;
    Long modelId;
    Date start;
    Date end;
    Long pickupOutletId;
    Long returnOutletId;
    XMLGregorianCalendar startXML;
    XMLGregorianCalendar endXML;

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Holiday Reservation Client ***\n");
            System.out.println("1. Login");
            System.out.println("2. Exit\n");
            response = 0;

            while (response != 1) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        if (currentPartner != null) {
                            menuPartner();
                        }
                    } catch (InvalidLoginCredentialsException_Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again.\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    private void menuPartner() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Holiday Reservation System :: Partner Menu ***\n");
            System.out.println("You are logged in as " + currentPartner.getPartnerName() + ".\n");
            System.out.println("1: Search Car");
            System.out.println("2. View All Reservations");
            System.out.println("3. View Reservation Details");
            System.out.println("4. Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.println("> ");
                response = sc.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    doViewAllReservations();
                } else if (response == 3) {
                    doViewReservationDetails();
                } else if (response == 4) {
                    currentPartner = null;
                    break;
                } else {
                    System.out.println("Invalid option, please try again.\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialsException_Exception {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Holiday Reservation Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentPartner = login(username, password);
        } else {
            System.out.println("Missing login credentials!");
        }
    }

    private void doSearchCar() {
        Scanner sc = new Scanner(System.in);
        String response = "";
        categoryId = null;
        modelId = null;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        start = null;
        end = null;
        pickupOutletId = null;
        returnOutletId = null;
        GregorianCalendar gc = new GregorianCalendar();
        startXML = null;
        endXML = null;

        System.out.println("*** CaRMS Reservation Client :: Search Car ***\n");

        List<CarCategory> categories = retrieveAllCarCategories();
        System.out.println("List of car categories:");
        Print.printListCarCategories(categories);

        while (true) {
            System.out.print("Enter desired car category> ");
            response = sc.nextLine().trim();
            if (Long.parseLong(response) < 0) {
                System.out.println("Invalid response, please try again");
            } else {
                categoryId = Long.parseLong(response);
                break;
            }
        }
        List<CarModel> carModels = retrieveCarModelsByCategory(categoryId);
        if (carModels.isEmpty()) {
            System.out.println("No models available");
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
            return;
        }
        Print.printListCarModels(carModels);
        while (true) {
            System.out.print("Enter desired car model (Enter 0 for any)> ");
            response = sc.nextLine().trim();
            if (Long.parseLong(response) < 0) {
                System.out.println("Invalid response, please try again");
            } else {
                modelId = Long.parseLong(response);
                break;
            }
        }
        while (true) {
            try {
                System.out.print("Enter pickup date and time (dd/mm/yyyy hh:mm AM/PM)> ");
                start = inputDateFormat.parse(sc.nextLine().trim());
                gc.setTime(start);
                startXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
                System.out.print("Enter return date and time (dd/mm/yyyy hh:mm AM/PM)> ");
                end = inputDateFormat.parse(sc.nextLine().trim());
                gc.setTime(end);
                endXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
                break;
            } catch (ParseException ex) {
                System.out.println("Invalid date time input, please try again");
            } catch (DatatypeConfigurationException ex) {
                ex.printStackTrace();
            }
        }
        List<Outlet> outlets = retrieveAllOutlets();
        Print.printListOutlets(outlets);
        System.out.print("\nEnter pickup outlet ID> ");
        pickupOutletId = Long.parseLong(sc.nextLine().trim());
        System.out.print("Enter return outlet ID> ");
        returnOutletId = Long.parseLong(sc.nextLine().trim());
        if (modelId != 0) {
            try {
                BigDecimal finalRate = searchByCarModel(categoryId, modelId, startXML, endXML, pickupOutletId, returnOutletId);
                System.out.print("Car model available! Total rate: S$" + finalRate + ". Confirm booking? (Press 'Y' to confirm)> ");
                response = sc.nextLine().trim();
                if (response.toLowerCase().equals("y")) {
                    if (currentPartner.getPartnerType().value().equals("MANAGER")) {
                        doReserveCar(finalRate);
                    } else {
                        System.out.println("Error. Only manager can make reservations!");
                    }
                } else {
                    System.out.println("Reservation cancelled");
                }
            } catch (CarCategoryNotFoundException_Exception | CarModelNotFoundException_Exception | OutletNotFoundException_Exception
                    | NoRateFoundException_Exception | InsufficientInventoryException_Exception | OutletClosedException_Exception ex) {
                System.out.println("\n" + ex.getMessage());
            }
        } else {
            try {
                BigDecimal finalRate = searchByCarCategory(categoryId, startXML, endXML, pickupOutletId, returnOutletId);
                System.out.print("Car available! Total rate: S$" + finalRate + ". Confirm booking? (Press 'Y' to confirm)> ");
                response = sc.nextLine().trim();
                if (response.toLowerCase().equals("y")) {
                    if (currentPartner.getPartnerType().value().equals("MANAGER")) {
                        doReserveCar(finalRate);
                    } else {
                        System.out.println("Error. Only manager can make reservations!");
                    }
                } else {
                    System.out.println("Reservation cancelled");
                }
            } catch (CarCategoryNotFoundException_Exception | OutletNotFoundException_Exception | NoRateFoundException_Exception | InsufficientInventoryException_Exception | OutletClosedException_Exception ex) {
                System.out.println("\n" + ex.getMessage());
            }
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doReserveCar(BigDecimal totalRate) {
        Scanner sc = new Scanner(System.in);
        String input;
        Long partnerId = currentPartner.getPartnerId();
        System.out.print("Enter customer name> ");
        String externalCustName = sc.nextLine().trim();
        System.out.print("Enter credit card number> ");
        String ccNum = sc.nextLine().trim();
        System.out.println("Select option: 1. Immediate payment | 2. Deferred payment");

        while (true) {
            input = sc.nextLine().trim();
            if (input.equals("1")) {
                try {
                    confirmReservation(partnerId, ccNum, true, categoryId, modelId, startXML, endXML, pickupOutletId, returnOutletId, totalRate, externalCustName);
                    System.out.println("Reservation confirmed!");
                } catch (UnknownPersistenceException_Exception ex) {
                    System.out.println("\n" + ex.getMessage());
                }
                break;
            } else if (input.equals("2")) {
                try {
                    confirmReservation(partnerId, ccNum, false, categoryId, modelId, startXML, endXML, pickupOutletId, returnOutletId, totalRate, externalCustName);
                    System.out.println("Reservation confirmed!");
                } catch (UnknownPersistenceException_Exception ex) {
                    System.out.println("\n" + ex.getMessage());
                }
                break;
            } else {
                System.out.println("Invalid input, please try again.");
            }
        }
    }

    private void doViewReservationDetails() {
        Scanner sc = new Scanner(System.in);

        if (!currentPartner.getPartnerType().value().equals("MANAGER")) {
            System.out.println("Invalid access rights");
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
            return;
        }

        System.out.println("*** CaRMS Reservation Client :: View Reservation Details ***\n");

        System.out.print("Enter Booking ID> ");
        Long bookingId = sc.nextLong();
        sc.nextLine();

        try {
            RentalRecord record = viewReservationDetails(bookingId);
            Print.printRentalRecord(record);

            doCancelReservation(record);
        } catch (RentalRecordNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private void doCancelReservation(RentalRecord record) {
        Scanner sc = new Scanner(System.in);
        String response = "";
        System.out.println("\n1. Cancel Reservation");
        System.out.println("2. Back\n");

        System.out.print("> ");
        response = sc.nextLine().trim();

        if (response.equals("1")) {
            if (record.isCancelled()) {
                System.out.println("Booking already cancelled");
            } else {
                try {
                    System.out.println(cancelReservation(record.getRentalRecordId()));
                } catch (CancellationErrorException_Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    private void doViewAllReservations() {
        Scanner sc = new Scanner(System.in);

        if (!currentPartner.getPartnerType().value().equals("MANAGER")) {
            System.out.println("Invalid access rights");
            System.out.println("\nPress Enter to continue...");
            sc.nextLine();
            return;
        }
        
        System.out.println("*** Holiday Reservation Client :: View All Reservations ***\n");
        try {
            List<RentalRecord> records = retrieveAllPartnerReservations(currentPartner.getUsername());
            Print.printListRentalRecords(records);
        } catch (PartnerNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    private static Partner login(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialsException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.login(username, password);
    }

    private static java.util.List<ws.client.RentalRecord> retrieveAllPartnerReservations(java.lang.String username) throws PartnerNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveAllPartnerReservations(username);
    }

    private static RentalRecord viewReservationDetails(java.lang.Long bookingId) throws RentalRecordNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.viewReservationDetails(bookingId);
    }

    private static String cancelReservation(java.lang.Long recordId) throws CancellationErrorException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.cancelReservation(recordId);
    }

    private static java.util.List<ws.client.CarCategory> retrieveAllCarCategories() {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveAllCarCategories();
    }

    private static java.util.List<ws.client.Outlet> retrieveAllOutlets() {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveAllOutlets();
    }

    private static java.util.List<ws.client.CarModel> retrieveCarModelsByCategory(java.lang.Long categoryId) {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.retrieveCarModelsByCategory(categoryId);
    }

    private static BigDecimal searchByCarCategory(java.lang.Long categoryId, javax.xml.datatype.XMLGregorianCalendar start, javax.xml.datatype.XMLGregorianCalendar end, java.lang.Long pickupOutletId, java.lang.Long returnOutletId) throws CarCategoryNotFoundException_Exception, NoRateFoundException_Exception, InsufficientInventoryException_Exception, OutletClosedException_Exception, OutletNotFoundException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.searchByCarCategory(categoryId, start, end, pickupOutletId, returnOutletId);
    }

    private static BigDecimal searchByCarModel(java.lang.Long categoryId, java.lang.Long modelId, javax.xml.datatype.XMLGregorianCalendar start, javax.xml.datatype.XMLGregorianCalendar end, java.lang.Long pickupOutletId, java.lang.Long returnOutletId) throws OutletClosedException_Exception, CarModelNotFoundException_Exception, CarCategoryNotFoundException_Exception, NoRateFoundException_Exception, OutletNotFoundException_Exception, InsufficientInventoryException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        return port.searchByCarModel(categoryId, modelId, start, end, pickupOutletId, returnOutletId);
    }

    private static void confirmReservation(java.lang.Long partnerId, java.lang.String ccNum, java.lang.Boolean immediatePayment, java.lang.Long categoryId, java.lang.Long modelId, javax.xml.datatype.XMLGregorianCalendar start, javax.xml.datatype.XMLGregorianCalendar end, java.lang.Long pickupOutletId, java.lang.Long returnOutletId, java.math.BigDecimal totalRate, java.lang.String externalCustName) throws UnknownPersistenceException_Exception {
        ws.client.CaRMSWebService_Service service = new ws.client.CaRMSWebService_Service();
        ws.client.CaRMSWebService port = service.getCaRMSWebServicePort();
        port.confirmReservation(partnerId, ccNum, immediatePayment, categoryId, modelId, start, end, pickupOutletId, returnOutletId, totalRate, externalCustName);
    }
}
