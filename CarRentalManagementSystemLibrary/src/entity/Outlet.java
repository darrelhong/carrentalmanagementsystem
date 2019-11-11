package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author darre
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    
    @Column(length = 128, nullable = false, unique = true)
    @NotNull
    @Size(min = 1, max = 128)
    private String address;
    
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer openTime;
    
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer closeTime;

    @OneToMany(mappedBy = "outlet")
    private List<Employee> employees;

    @OneToMany(mappedBy = "outlet")
    private List<Car> cars;

    public Outlet() {
        employees = new ArrayList<>();
    }

    public Outlet(String address, Integer openTime, Integer closeTime) {
        this();
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public Long getId() {
        return getOutletId();
    }

    public void setId(Long id) {
        this.setOutletId(id);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOutletId() != null ? getOutletId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.getOutletId() == null && other.getOutletId() != null) || (this.getOutletId() != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + getOutletId() + " ]";
    }

    /**
     * @return the outletId
     */
    public Long getOutletId() {
        return outletId;
    }

    /**
     * @param outletId the outletId to set
     */
    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the openTime
     */
    public Integer getOpenTime() {
        return openTime;
    }

    /**
     * @param openTime the openTime to set
     */
    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
    }

    /**
     * @return the closeTime
     */
    public Integer getCloseTime() {
        return closeTime;
    }

    /**
     * @param closeTime the closeTime to set
     */
    public void setCloseTime(Integer closeTime) {
        this.closeTime = closeTime;
    }

    /**
     * @return the employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * @return the cars
     */
    @XmlTransient
    public List<Car> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

}
