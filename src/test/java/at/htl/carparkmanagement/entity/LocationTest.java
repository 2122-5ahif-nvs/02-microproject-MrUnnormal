package at.htl.carparkmanagement.entity;

import at.htl.carparkmanagement.repository.Datasource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.assertj.db.output.Outputs.output;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LocationTest {
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
    @Order(10)
    void testCreateLocationInDB() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        // Transaktion ... kleinste unteilbare zB Überweisung Aktion in einer Bank
        //      - von Konto A wird abgebucht
        //      - auf Konto B wird aufgebucht
        // Logical Unit of Work
        // arrange
        transactionManager.begin();
        em.createQuery("DELETE FROM Contract c").executeUpdate();
        em.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        em.createQuery("DELETE FROM Location l").executeUpdate();
        Location item = new Location("Garagen City", "4060");

        // act
        em.persist(item);
        transactionManager.commit();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "location");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);
    }

    @Test
    @Order(20)
    void testUpdateLocationWithoutMerge() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        Location item = new Location("Garagen City", "4060");
        transactionManager.begin();
        em.persist(item);
        item.setName("Garagen-City");
        transactionManager.commit();
        Table locationTable = new Table(Datasource.getDataSource(), "location");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(2);
        //fail("not yet implemented");
    }

    @Test
    void testEquals() {
        // arrange
        Location item1 = new Location(1L, "Garagen City", "4060");
        Location item2 = new Location(1L, "Garagen City", "4060");

        // act

        // assert
        assertThat(item1).isEqualTo(item2);
    }

    @Test
    void testToString() {
        // arrange
        Location item = new Location(1L, "Garagen City", "4060");

        // act
        String itemString = item.toString();

        // assert
        assertThat(itemString).isEqualTo("Location{id=1, name='Garagen City', zipcode='4060'}");
    }
}