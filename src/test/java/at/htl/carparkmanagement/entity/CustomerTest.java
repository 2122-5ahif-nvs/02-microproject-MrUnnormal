package at.htl.carparkmanagement.entity;

import at.htl.carparkmanagement.repository.Datasource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CustomerTest {

    @Inject
    EntityManager em;

    @Inject
    UserTransaction transactionManager;

    @BeforeEach
    @Transactional
    void setUp() {
        em.createQuery("DELETE FROM Contract c").executeUpdate();
        em.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        em.createQuery("DELETE FROM Location l").executeUpdate();
        em.createQuery("DELETE FROM Customer c").executeUpdate();
        em.clear();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateCustomerInDB() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        // arrange
        Customer item = new Customer("Max", "Mustermann", true);

        // act
        transactionManager.begin();
        em.persist(item);
        transactionManager.commit();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "customer");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);
    }

    @Test
    void testEquals() {
        // arrange
        Customer item1 = new Customer(1L, "Max", "Mustermann", true);
        Customer item2= new Customer(1L, "Max", "Mustermann", true);

        // act

        // assert
        assertThat(item1).isEqualTo(item2);
    }

    @Test
    void testToString() {
        // arrange
        Customer item = new Customer(1L, "Max", "Mustermann", true);

        // act
        String itemString = item.toString();

        // assert
        assertThat(itemString).isEqualTo("Customer{id=1, firstname='Max', lastname='Mustermann', environment='true'}");
    }
}