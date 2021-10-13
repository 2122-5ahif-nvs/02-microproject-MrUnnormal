package at.htl.carparkmanagement.entity;

import at.htl.carparkmanagement.repository.LocalDateAdapter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@XmlRootElement
@Schema(description = "In a contract will be saved which customer rented which parkingspot for how long and when the customer paid.")
public class Contract {
    @Id
    @Schema(required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    private Long id;

    @Schema(required = true)
    @ManyToOne(optional = false)
    private Customer customer;

    @Schema(required = true)
    @ManyToOne(optional = false)
    private Parkingspot parkingspot;

    @Schema(required = true)
    @JsonbProperty("start_date")
    @Column(name = "co_startDate", nullable = false)
    @XmlJavaTypeAdapter(type=LocalDate.class, value= LocalDateAdapter.class)
    private LocalDate startDate;

    @JsonbProperty("end_date")
    @Column(name = "co_endDate")
    @XmlJavaTypeAdapter(type=LocalDate.class, value=LocalDateAdapter.class)
    private LocalDate endDate;

    @JsonbProperty("pay_date")
    @Column(name = "co_payDate")
    @XmlJavaTypeAdapter(type=LocalDate.class, value=LocalDateAdapter.class)
    private LocalDate payDate;

    public Contract(Long id, Customer customer, Parkingspot parkingspot, LocalDate startDate) {
        this.id = id;
        this.customer = customer;
        this.parkingspot = parkingspot;
        this.startDate = startDate;
    }

    public Contract(Customer customer, Parkingspot parkingspot, LocalDate startDate) {
        this.customer = customer;
        this.parkingspot = parkingspot;
        this.startDate = startDate;
    }

    public Contract() {
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Parkingspot getParkingspot() {
        return parkingspot;
    }

    public void setParkingspot(Parkingspot parkingspot) {
        this.parkingspot = parkingspot;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return getId().equals(contract.getId()) &&
                getCustomer().equals(contract.getCustomer()) &&
                getParkingspot().equals(contract.getParkingspot()) &&
                getStartDate().equals(contract.getStartDate()) &&
                Objects.equals(getEndDate(), contract.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCustomer(), getParkingspot(), getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", customer=" + customer +
                ", parkingspot=" + parkingspot +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
