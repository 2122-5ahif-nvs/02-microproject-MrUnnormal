package at.htl.carparkmanagement.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity
@XmlRootElement
@Schema(description = "Saves details for a Location")
public class Location {
    @Schema(required = true)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "l_id")
    private Long id;

    @Schema(required = true)
    @Column(name = "l_name", nullable = false)
    private String name;

    @Schema(required = true)
    @Column(name = "l_zipcode", nullable = false)
    private String zipcode;

    public Location() {
    }

    public Location(Long id, String name, String zipcode) {
        this.id = id;
        this.name = name;
        this.zipcode = zipcode;
    }

    public Location(String name, String zipcode) {
        this.name = name;
        this.zipcode = zipcode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return getId().equals(location.getId()) &&
                getName().equals(location.getName()) &&
                getZipcode().equals(location.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getZipcode());
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
