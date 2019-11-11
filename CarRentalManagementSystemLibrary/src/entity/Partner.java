package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;
import util.enumeration.PartnerType;

/**
 *
 * @author darre
 */
@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(length = 32, nullable = false)
    private String partnerName;
    @Column(length = 32, nullable = false, unique = true)
    private String username;
    @Column(length = 64, nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private PartnerType partnerType;

    @OneToMany(mappedBy = "partner")
    private List<RentalRecord> rentalRecords;

    public Partner() {
        this.rentalRecords = new ArrayList<>();
    }

    public Partner(String partnerName, String username, String password, PartnerType partnerType) {
        this();
        this.partnerName = partnerName;
        this.username = username;
        this.password = password;
        this.partnerType = partnerType;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerId != null ? partnerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerId fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.partnerId == null && other.partnerId != null) || (this.partnerId != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Partner[ id=" + partnerId + " ]";
    }

    /**
     * @return the partnerName
     */
    public String getPartnerName() {
        return partnerName;
    }

    /**
     * @param partnerName the partnerName to set
     */
    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the rentalRecords
     */
    @XmlTransient
    public List<RentalRecord> getRentalRecords() {
        return rentalRecords;
    }

    /**
     * @param rentalRecords the rentalRecords to set
     */
    public void setRentalRecords(List<RentalRecord> rentalRecords) {
        this.rentalRecords = rentalRecords;
    }

    /**
     * @return the partnerType
     */
    public PartnerType getPartnerType() {
        return partnerType;
    }

    /**
     * @param partnerType the partnerType to set
     */
    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

}
