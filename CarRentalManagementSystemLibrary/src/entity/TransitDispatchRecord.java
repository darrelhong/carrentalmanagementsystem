package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class TransitDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean completed;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet fromOutlet;
    
    @OneToOne(optional = false)
    private RentalRecord rentalRecord;
    
    @ManyToOne(optional = true)
    private Employee employee;

    public TransitDispatchRecord() {
        this.completed = false;
    }
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransitDispatchRecord)) {
            return false;
        }
        TransitDispatchRecord other = (TransitDispatchRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDispatchRecord[ id=" + id + " ]";
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
     * @return the rentalRecord
     */
    public RentalRecord getRentalRecord() {
        return rentalRecord;
    }

    /**
     * @param rentalRecord the rentalRecord to set
     */
    public void setRentalRecord(RentalRecord rentalRecord) {
        this.rentalRecord = rentalRecord;
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the completed
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * @param completed the completed to set
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
}
