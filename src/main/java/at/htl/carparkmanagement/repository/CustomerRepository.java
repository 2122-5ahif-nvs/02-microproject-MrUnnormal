package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    @Transactional
    public Customer find(Long id) {
        try {
            return this.findById(id);
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Transactional
    public Customer add(Customer customer) {
        if (customer.getId() != null) {
            return null;
        }
        this.persist(customer);
        return customer;
    }

    @Transactional
    public Customer update(Customer customer) {
        if (customer.getId() != null) {
            Customer update = this.find(customer.getId());
            update.setFirstname(customer.getFirstname());
            update.setLastname(customer.getLastname());
            update.setIsPrivat(customer.getIsPrivat());
            return update;
        }
        return this.add(customer);
    }

    public List<Customer> getCustomerList() {
        return this.findAll().list();
    }

    @Transactional
    public boolean deleteCustomer(Long id) {
        Customer delete = this.find(id);
        if (delete == null) {
            return false;
        }
        this.delete(delete);
        return true;
    }
}
