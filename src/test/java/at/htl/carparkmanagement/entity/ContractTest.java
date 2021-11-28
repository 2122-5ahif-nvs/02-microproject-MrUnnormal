package at.htl.carparkmanagement.entity;

import at.htl.carparkmanagement.repository.Datasource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.*;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ContractTest {
    @Inject
    EntityManager em;

    @Inject
    UserTransaction transactionManager;

    @BeforeEach
    void setUp() {
        em.clear();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateContractInDB() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        // arrange
        Customer customer = new Customer("Max", "Mustermann", true);
        Location location = new Location( "Garagen City", "4060");
        Parkingspot parkingspot = new Parkingspot( "Default", location, 19.99, 4);
        Contract item = new Contract( customer, parkingspot, LocalDate.now());

        // act
        transactionManager.begin();
        em.persist(customer);
        em.persist(location);
        em.persist(parkingspot);
        em.persist(item);
        transactionManager.commit();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "contract");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);
    }

    @Test
    void testEquals() {
        // arrange
        Customer customer = new Customer(1L, "Max", "Mustermann", true);
        Location location1 = new Location(1L, "Garagen City", "4060");
        Parkingspot parkingspot = new Parkingspot(1L, "Default", location1, 19.99, 4);
        Contract item1 = new Contract(1L, customer, parkingspot, LocalDate.now());
        Contract item2 = new Contract(1L, customer, parkingspot, LocalDate.now());

        // act

        // assert
        assertThat(item1).isEqualTo(item2);
    }

    @Test
    void testToString() {
        // arrange
        Customer customer = new Customer(1L, "Max", "Mustermann", true);
        Location location1 = new Location(1L, "Garagen City", "4060");
        Parkingspot parkingspot = new Parkingspot(1L, "Default", location1, 19.99, 4);
        Contract item = new Contract(1L, customer, parkingspot, LocalDate.now());

        // act
        String itemString = item.toString();

        // assert
        assertThat(itemString).isEqualTo("Contract{id=1, customer=Customer{id=1, firstname='Max', lastname='Mustermann', environment='true'}," +
                " parkingspot=Parkingspot{id=1, type='Default', location=Location{id=1, name='Garagen City', zipcode='4060'}}, startDate=" + LocalDate.now() + ", endDate=null}");
    }
}