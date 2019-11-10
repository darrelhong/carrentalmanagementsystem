package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author darre
 */
@Entity
public class ExternalCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long externalCustomerId;
    @Column(length = 32, nullable = false)
    private String name;

    public ExternalCustomer() {
    }

    public ExternalCustomer(String name) {
        this.name = name;
    }

    public Long getExternalCustomerId() {
        return externalCustomerId;
    }

    public void setExternalCustomerId(Long externalCustomerId) {
        this.externalCustomerId = externalCustomerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (externalCustomerId != null ? externalCustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the externalCustomerId fields are not set
        if (!(object instanceof ExternalCustomer)) {
            return false;
        }
        ExternalCustomer other = (ExternalCustomer) object;
        if ((this.externalCustomerId == null && other.externalCustomerId != null) || (this.externalCustomerId != null && !this.externalCustomerId.equals(other.externalCustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CaRMSCustomer[ id=" + externalCustomerId + " ]";
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

}
