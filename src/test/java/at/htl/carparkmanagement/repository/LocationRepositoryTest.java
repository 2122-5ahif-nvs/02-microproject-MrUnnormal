package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Location;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class LocationRepositoryTest {
    @Inject
    LocationRepository locationRepository;

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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindById() {
        // arrange
        Location newItem = new Location("Garagen City", "4060");

        // act
        newItem = locationRepository.add(newItem);
        List<Location> locationList = locationRepository.getLocationList();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "location");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);

        assertThat(locationList).isNotNull();
        assertThat(locationList.size()).isEqualTo(1);
        assertThat(locationList.get(0)).isEqualTo(newItem);
        assertThat(locationRepository.findById(2L)).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
        assertThat(newItem.getId()).isEqualTo(2L);


    }

    @Test
    void testAddLocation() {
        // arrange
        Location newItem = new Location("Garagen City", "4060");

        // act
        newItem = locationRepository.add(newItem);
        List<Location> locationList = locationRepository.getLocationList();
        Location shouldBeNull = locationRepository.add(newItem);

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "location");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);

        assertThat(locationList).isNotNull();
        assertThat(locationList.size()).isEqualTo(1);
        assertThat(locationList.get(0)).isEqualTo(newItem);
        assertThat(newItem.getId()).isNotNull();
        assertThat(newItem.getId()).isEqualTo(3L);
        assertThat(shouldBeNull).isNull();   // Make sure it cannot be added twice


    }

    @Test
    void testUpdateLocation() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        // arrange
        Location newItem = new Location("Garagen City", "4060");

        // act
        transactionManager.begin();
        newItem = locationRepository.add(newItem);
        List<Location> locationListBefore = locationRepository.getLocationList();
        newItem.setName("Garagen-City");
        transactionManager.commit();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "location");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);

        assertThat(locationListBefore).isNotNull();
        assertThat(newItem.getId()).isNotNull();
        assertThat(newItem.getId()).isEqualTo(4L);
        assertThat(locationRepository.findById(4L).getName()).isEqualTo("Garagen-City");
    }

    @Test
    @Transactional
    void testDeleteLocation() {
        // arrange
        Location newItem = new Location("Garagen City", "4060");

        // act
        newItem = locationRepository.add(newItem);
        locationRepository.deleteLocation(newItem.getId());
        List<Location> locationList = locationRepository.getLocationList();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "location");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(0);

        assertThat(locationList).isEqualTo(new LinkedList<Location>());
        assertThat(locationList.size()).isEqualTo(0);
        assertThat(newItem.getId()).isNotNull();
        assertThat(newItem.getId()).isEqualTo(1L);
    }
}