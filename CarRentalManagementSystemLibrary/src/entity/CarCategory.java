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

/**
 *
 * @author darre
 */
@Entity
public class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carCategoryId;
    @Column(length = 32, nullable = false, unique = true)
    private String carCategory;
    
    @OneToMany(mappedBy = "carCategory")
    private List<CarModel> carModels;
    
    @OneToMany(mappedBy = "carCategory")
    private List<Car> cars;

    public CarCategory() {
        this.cars = new ArrayList<>();
        this.carModels = new ArrayList<>();
    }

    public CarCategory(String carCategory) {
        this();
        this.carCategory = carCategory;
    }

    public Long getCarCategoryId() {
        return carCategoryId;
    }

    public void setCarCategoryId(Long carCategoryId) {
        this.carCategoryId = carCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carCategoryId != null ? carCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carCategoryId fields are not set
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.carCategoryId == null && other.carCategoryId != null) || (this.carCategoryId != null && !this.carCategoryId.equals(other.carCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarCategory[ id=" + carCategoryId + " ]";
    }

    /**
     * @return the carCategory
     */
    public String getCarCategory() {
        return carCategory;
    }

    /**
     * @param carCategory the carCategory to set
     */
    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }

    /**
     * @return the carModels
     */
    public List<CarModel> getCarModels() {
        return carModels;
    }

    /**
     * @param carModels the carModels to set
     */
    public void setCarModels(List<CarModel> carModels) {
        this.carModels = carModels;
    }

    /**
     * @return the cars
     */
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
