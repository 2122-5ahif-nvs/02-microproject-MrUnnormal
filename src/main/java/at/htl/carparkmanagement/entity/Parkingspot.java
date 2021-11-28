package at.htl.carparkmanagement.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity
@XmlRootElement
@Schema(description = "Saves details for a parkingspot")
@NamedQuery(name="Parkingspot.getFree", query="SELECT p FROM Parkingspot p WHERE p NOT IN (SELECT c.parkingspot FROM Contract c WHERE c.parkingspot.location = :location)")
public class Parkingspot {
    @Id
    @Schema(required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    @Schema(required = true)
    @Column(name = "p_type", nullable = false)
    private String type;

    @Schema(required = true)
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Location location;

    @Schema(required = true)
    @Column(name = "p_priceperday", nullable = false)
    private double pricePerDay;

    @Schema(required = true)
    @Column(name = "p_position", nullable = false)
    private int position;

    public Parkingspot(Long id, String type, Location location, double pricePerDay, int position) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.pricePerDay = pricePerDay;
        this.position = position;
    }

    public Parkingspot(String type, Location location, double pricePerDay, int position) {
        this.type = type;
        this.location = location;
        this.pricePerDay = pricePerDay;
        this.position = position;
    }

    public Parkingspot() {    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Parkingspot{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parkingspot that = (Parkingspot) o;
        return getId().equals(that.getId()) &&
                Objects.equals(getType(), that.getType()) &&
                getLocation().equals(that.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getLocation());
    }
}
