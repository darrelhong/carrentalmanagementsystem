package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author darre
 */
@Entity
public class RentalRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRecordId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endDateTime;
    @Column(nullable = false, length = 16)
    private String ccNum;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    @Column(precision = 19, scale = 4)
    private BigDecimal amountToRefund;
    @Column(precision = 19, scale = 4)
    private BigDecimal penaltyAmount;
    @Column(nullable = false)
    private Boolean paid;
    @Column(nullable = false)
    private Boolean pickedUp;
    @Column(nullable = false)
    private Boolean returned;
    @Column(nullable = false)
    private Boolean cancelled;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet fromOutlet;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet toOutlet;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Customer customer;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private ExternalCustomer externalCustomer;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Partner partner;

    @ManyToOne(optional = true)
    private Car car;

    @ManyToOne(optional = true)
    private CarModel carModel;

    @ManyToOne(optional = true)
    private CarCategory carCategory;

    public RentalRecord() {
        this.pickedUp = false;
        this.returned = false;
        this.cancelled = false;
    }

    // For data init
    public RentalRecord(Date startDateTime, Date endDateTime, String ccNum, BigDecimal amount, Boolean paid, Boolean pickedUp, Boolean returned, Boolean cancelled, Outlet fromOutlet, Outlet toOutlet, Customer customer, CarModel carModel) {
        this();
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.ccNum = ccNum;
        this.amount = amount;
        this.paid = paid;
        this.pickedUp = pickedUp;
        this.returned = returned;
        this.cancelled = cancelled;
        this.fromOutlet = fromOutlet;
        this.toOutlet = toOutlet;
        this.customer = customer;
        this.carModel = carModel;
    }

    public RentalRecord(Date startDateTime, Date endDateTime, String ccNum, BigDecimal amount, Boolean paid, Boolean pickedUp, Boolean returned, Boolean cancelled, Outlet fromOutlet, Outlet toOutlet, Customer customer, CarCategory category) {
        this();
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.ccNum = ccNum;
        this.amount = amount;
        this.paid = paid;
        this.pickedUp = pickedUp;
        this.returned = returned;
        this.cancelled = cancelled;
        this.fromOutlet = fromOutlet;
        this.toOutlet = toOutlet;
        this.customer = customer;
        this.carCategory = category;
    }

    public Long getRentalRecordId() {
        return rentalRecordId;
    }

    public void setRentalRecordId(Long rentalRecordId) {
        this.rentalRecordId = rentalRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRentalRecordId() != null ? getRentalRecordId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRecordId fields are not set
        if (!(object instanceof RentalRecord)) {
            return false;
        }
        RentalRecord other = (RentalRecord) object;
        if ((this.getRentalRecordId() == null && other.getRentalRecordId() != null) || (this.getRentalRecordId() != null && !this.rentalRecordId.equals(other.rentalRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRecord[ id=" + getRentalRecordId() + " ]";
    }

    /**
     * @return the startDateTime
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public Date getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the ccNum
     */
    public String getCcNum() {
        return ccNum;
    }

    /**
     * @param ccNum the ccNum to set
     */
    public void setCcNum(String ccNum) {
        this.ccNum = ccNum;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the amountToRefund
     */
    public BigDecimal getAmountToRefund() {
        return amountToRefund;
    }

    /**
     * @param amountToRefund the amountToRefund to set
     */
    public void setAmountToRefund(BigDecimal amountToRefund) {
        this.amountToRefund = amountToRefund;
    }

    /**
     * @return the pickedUp
     */
    public Boolean getPickedUp() {
        return pickedUp;
    }

    /**
     * @param pickedUp the pickedUp to set
     */
    public void setPickedUp(Boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    /**
     * @return the returned
     */
    public Boolean getReturned() {
        return returned;
    }

    /**
     * @param returned the returned to set
     */
    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    /**
     * @return the cancelled
     */
    public Boolean getCancelled() {
        return cancelled;
    }

    /**
     * @param cancelled the cancelled to set
     */
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * @return the fromOutlet
     */
    public Outlet getFromOutlet() {
        return fromOutlet;
    }

    /**
     * @param fromOutlet the fromOutlet to set
     */
    public void setFromOutlet(Outlet fromOutlet) {
        this.fromOutlet = fromOutlet;
    }

    /**
     * @return the toOutlet
     */
    public Outlet getToOutlet() {
        return toOutlet;
    }

    /**
     * @param toOutlet the toOutlet to set
     */
    public void setToOutlet(Outlet toOutlet) {
        this.toOutlet = toOutlet;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * @return the carModel
     */
    public CarModel getCarModel() {
        return carModel;
    }

    /**
     * @param carModel the carModel to set
     */
    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    /**
     * @return the paid
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * @return the penaltyAmount
     */
    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    /**
     * @param penaltyAmount the penaltyAmount to set
     */
    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    /**
     * @return the carCategory
     */
    public CarCategory getCarCategory() {
        return carCategory;
    }

    /**
     * @param carCategory the carCategory to set
     */
    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * @return the externalCustomer
     */
    public ExternalCustomer getExternalCustomer() {
        return externalCustomer;
    }

    /**
     * @param externalCustomer the externalCustomer to set
     */
    public void setExternalCustomer(ExternalCustomer externalCustomer) {
        this.externalCustomer = externalCustomer;
    }

}
