package at.htl.carparkmanagement.entity;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity
@XmlRootElement
@Schema(description = "Saves all details for a customer")
public class Customer {
    @Id
    @Schema(required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Schema(required = true)
    @Column(name = "c_firstname", nullable = false)
    private String firstname;

    @Schema(required = true)
    @Column(name = "c_lastname", nullable = false)
    private String lastname;

    @Schema(required = true)
    @Column(name = "c_isPrivate", nullable = false)
    private boolean isPrivat;

    public Customer(Long id, String firstname, String lastname, boolean isPrivat) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isPrivat = isPrivat;
    }

    public Customer(String firstname, String lastname, boolean isPrivat) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.isPrivat = isPrivat;
    }

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean getIsPrivat() {
        return isPrivat;
    }

    public void setIsPrivat(boolean environment) {
        this.isPrivat = environment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return getId().equals(customer.getId()) &&
                getFirstname().equals(customer.getFirstname()) &&
                getLastname().equals(customer.getLastname()) &&
                getIsPrivat() == customer.getIsPrivat();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstname(), getLastname(), getIsPrivat());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", environment='" + isPrivat + '\'' +
                '}';
    }
}
