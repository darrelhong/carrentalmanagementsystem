package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author darre
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(length = 64, nullable = false)
    private String name;
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal rate;
    private Boolean isPromo;
    private Boolean disabled;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> validityPeriod;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    public RentalRate() {
        this.disabled = false;
        this.validityPeriod = new ArrayList<>();
    }

    public RentalRate(String name, BigDecimal rate, Boolean isPromo, List<Integer> validityPeriod) {
        this();
        this.name = name;
        this.rate = rate;
        this.isPromo = isPromo;
        this.validityPeriod = validityPeriod;
    }

    public void addValidDay(Integer day) {
        this.validityPeriod.add(day);
    }

    public boolean removeValidDay(Integer day) {
        return this.validityPeriod.remove(day);
    }

    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRentalRateId() != null ? getRentalRateId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.getRentalRateId() == null && other.getRentalRateId() != null) || (this.getRentalRateId() != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + getRentalRateId() + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the validityPeriod
     */
    public List<Integer> getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(List<Integer> validityPeriod) {
        this.validityPeriod = validityPeriod;
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
     * @return the isPromo
     */
    public Boolean getIsPromo() {
        return isPromo;
    }

    /**
     * @param isPromo the isPromo to set
     */
    public void setIsPromo(Boolean isPromo) {
        this.isPromo = isPromo;
    }

}
