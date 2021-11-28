package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Contract;
import at.htl.carparkmanagement.entity.Customer;
import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.entity.Parkingspot;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ContractRepositoryTest {
    @Inject
    ContractRepository contractRepository;

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
        Customer customer = new Customer("Max", "Mustermann", true);
        Location location1 = new Location("Garagen City", "4060");
        Parkingspot parkingspot = new Parkingspot("Default", location1, 19.99, 4);
        Contract newItem = new Contract(customer, parkingspot, LocalDate.now());

        // act
        newItem = contractRepository.add(newItem);
        List<Contract> contractList = contractRepository.findAll().list();

        // assert
        assertThat(contractList).isNotNull();
        assertThat(contractList.size()).isEqualTo(1);
        assertThat(contractList.get(0)).isEqualTo(newItem);
        assertThat(contractRepository.findById(newItem.getId())).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
    }

    @Test
    void testAddContract() {
        // arrange
        Customer customer = new Customer("Max", "Mustermann", true);
        Location location1 = new Location("Garagen City", "4060");
        Parkingspot parkingspot = new Parkingspot("Default", location1, 19.99, 4);
        Contract newItem = new Contract(customer, parkingspot, LocalDate.now());

        // act
        newItem = contractRepository.add(newItem);
        List<Contract> contractList = contractRepository.findAll().list();
        Contract shouldBeNull = contractRepository.add(newItem);

        // assert
        assertThat(contractList).isNotNull();
        assertThat(contractList.size()).isEqualTo(1);
        assertThat(contractList.get(0)).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
        assertThat(shouldBeNull).isNull();   // Make sure it cannot be added twice
    }

    @Test
    void testUpdateContract() {
        // arrange
        Customer customer = new Customer("Max", "Mustermann", true);
        Location location1 = new Location("Garagen City", "4060");
        Parkingspot parkingspot = new Parkingspot("Default", location1, 19.99, 4);
        Contract newItem = new Contract(customer, parkingspot, LocalDate.now());

        // act
        newItem = contractRepository.add(newItem);
        List<Contract> contractListBefore = contractRepository.findAll().list();
        newItem.setEndDate(LocalDate.now().plusMonths(5));
        newItem = contractRepository.update(newItem);
        List<Contract> contractListAfter = contractRepository.findAll().list();

        // assert
        assertThat(contractListBefore).isNotNull();
        assertThat(contractListAfter).isNotNull();
        assertThat(contractListAfter.size()).isEqualTo(1);
        assertThat(contractRepository.find(newItem.getId())).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
    }

    @Test
    void testDeleteContract() {
        // arrange
        Customer customer = new Customer("Max", "Mustermann", true);
        Location location1 = new Location("Garagen City", "4060");
        Parkingspot parkingspot = new Parkingspot( "Default", location1, 19.99, 4);
        Contract newItem = new Contract(customer, parkingspot, LocalDate.now());

        // act
        newItem = contractRepository.add(newItem);

        contractRepository.deleteContract(newItem.getId());
        List<Contract> customerList = contractRepository.findAll().list();

        // assert
        assertThat(customerList.size()).isEqualTo(0);
        assertThat(newItem).isNotNull();
    }
}
