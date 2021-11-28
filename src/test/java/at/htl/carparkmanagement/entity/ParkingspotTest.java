package at.htl.carparkmanagement.entity;

import at.htl.carparkmanagement.repository.Datasource;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ParkingspotTest {
    @Inject
    EntityManager em;

    @Inject
    UserTransaction transactionManager;

    @BeforeEach
    void setUp() {
        em.clear();
    }

    @AfterEach
    void tearDown() {}

    @Test
    void testEquals() {
        // arrange
        Location location = new Location(1L, "Garagen City", "4060");
        Parkingspot item1 = new Parkingspot(1L, "Default", location, 19.99, 4);
        Parkingspot item2 = new Parkingspot(1L, "Default", location, 19.99, 4);

        // act

        // assert
        assertThat(item1).isEqualTo(item2);
    }

    @Test
    void testCreateParkingspotInDB() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        // arrange
        transactionManager.begin();
        em.createQuery("DELETE FROM Contract c").executeUpdate();
        em.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        em.createQuery("DELETE FROM Location l").executeUpdate();
        Location location = new Location("Garagen City", "4060");
        Parkingspot item = new Parkingspot("Default", location, 19.99, 4);

        // act

        em.persist(location);
        em.persist(item);
        transactionManager.commit();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "parkingspot");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);
    }

    @Test
    void testToString() {
        // arrange
        Location location = new Location(1L, "Garagen City", "4060");
        Parkingspot item = new Parkingspot(1L, "Default", location, 19.99, 4);

        // act
        String itemString = item.toString();

        // assert
        assertThat(itemString).isEqualTo("Parkingspot{id=1, type='Default', location=Location{id=1, name='Garagen City', zipcode='4060'}}");
    }
}