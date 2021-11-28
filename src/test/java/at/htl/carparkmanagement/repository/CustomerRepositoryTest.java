package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Customer;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class CustomerRepositoryTest {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    EntityManager entityManager;

    @Inject
    UserTransaction transactionManager;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM Contract c").executeUpdate();
        entityManager.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        entityManager.createQuery("DELETE FROM Location l").executeUpdate();
        entityManager.createQuery("DELETE FROM Customer c").executeUpdate();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindById() {
        // arrange
        Customer newItem = new Customer("Max", "Mustermann", true);

        // act
        newItem = customerRepository.add(newItem);
        List<Customer> customerList = customerRepository.getCustomerList();

        // assert
        assertThat(customerList).isNotNull();
        assertThat(customerList.size()).isEqualTo(1);
        assertThat(customerList.get(0)).isEqualTo(newItem);
        assertThat(customerRepository.findById(newItem.getId())).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
    }

    @Test
    void testAddCustomer() {
        // arrange
        Customer newItem = new Customer("Max", "Mustermann", true);

        // act
        newItem = customerRepository.add(newItem);
        List<Customer> customerList = customerRepository.getCustomerList();
        Customer shouldBeNull = customerRepository.add(newItem);

        // assert
        assertThat(customerList).isNotNull();
        assertThat(customerList.size()).isEqualTo(1);
        assertThat(customerList.get(0)).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
        assertThat(shouldBeNull).isNull();   // Make sure it cannot be added twice
    }

    @Test
    void testUpdateCustomer() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        // arrange
        Customer newItem = new Customer("Max", "Mustermann", true);

        // act
        transactionManager.begin();
        newItem = customerRepository.add(newItem);
        List<Customer> customerListBefore = customerRepository.getCustomerList();
        newItem.setLastname("Musterfrau");
        customerRepository.update(newItem);
        List<Customer> customerListAfter = customerRepository.getCustomerList();
        transactionManager.commit();

        // assert
        assertThat(customerListBefore).isNotNull();
        assertThat(customerListAfter).isNotNull();
        assertThat(customerListAfter.size()).isEqualTo(1);
        assertThat(customerListAfter.get(0)).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
    }

    @Test
    void testDeleteCustomer() {
        // arrange
        Customer newItem = new Customer("Max", "Mustermann", true);

        // act
        newItem = customerRepository.add(newItem);
        List<Customer> customerList = customerRepository.getCustomerList();
        customerRepository.deleteCustomer(newItem.getId());
        List<Customer> customerList2 = customerRepository.getCustomerList();

        // assert
        assertThat(customerList.size()).isEqualTo(1);
        assertThat(customerList2.size()).isEqualTo(0);
        assertThat(newItem).isNotNull();
    }
}
