package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Contract;
import at.htl.carparkmanagement.entity.Customer;
import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.entity.Parkingspot;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.maven.model.Repository;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.output.Outputs.output;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ParkingspotRepositoryTest {
    @Inject
    ParkingspotRepository parkingspotRepository;

    @Inject
    ContractRepository contractRepository;

    @Inject
    LocationRepository locationRepository;

    @Inject
    EntityManager entityManager;

    @Inject
    UserTransaction transactionManager;

    @BeforeAll
    static void beforeAll() {
    }

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
    @Order(200)
    void testUpdateParkingspot() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        // arrange
        Location location = new Location("Garagen City", "4060");
        Parkingspot new1 = new Parkingspot("Default", location, 19.99, 4);

        // act
        transactionManager.begin();
        new1 = parkingspotRepository.add(new1);
        new1.setType("Yes");
        int size = parkingspotRepository.getParkingspotList().size();
        transactionManager.commit();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "parkingspot");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);

        assertThat(new1).isNotNull();
        assertThat(new1.getId()).isNotNull();
        assertThat(size).isEqualTo(1);
    }

    @Test
    @Order(100)
    void testAddParkingspot() {
        // arrange
        Location location = new Location("Garagen City", "4060");
        Parkingspot new1 = new Parkingspot("Default", location, 19.99, 4);
        //Parkingspot new2 = new Parkingspot("Default", location, 19.99, 4);

        // act
        new1 = parkingspotRepository.add(new1);
        Parkingspot shouldBeNull = parkingspotRepository.add(new1);
        int size = parkingspotRepository.getParkingspotList().size();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "parkingspot");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);

        assertThat(new1.getId()).isNotNull();
        assertThat(size).isEqualTo(1);
        assertThat(new1.getId()).isNotNull();
        assertThat(new1.getId()).as("This might happen when you don't run all the tests").isEqualTo(25);
        assertThat(shouldBeNull).isNull();   // Make sure it cannot be added twice
    }

    @Test
    @Order(400)
    void testDeleteParkingspot() {
        // arrange
        Location location = new Location("Garagen City", "4060");
        Parkingspot new1 = new Parkingspot("Default", location, 19.99, 4);

        // act
        new1 = parkingspotRepository.add(new1);
        int sizeAdd = parkingspotRepository.getParkingspotList().size();

        parkingspotRepository.deleteParkingspot(new1.getId());
        int sizeDelete = parkingspotRepository.getParkingspotList().size();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "parkingspot");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(0);

        assertThat(new1).isNotNull();
        assertThat(new1.getId()).isNotNull();
        assertThat(sizeAdd).isEqualTo(1);

        // after delete
        assertThat(sizeDelete).isEqualTo(0);
    }

    @Test
    @Order(600)
    void testFindById() {
        // arrange
        Location location = new Location("Garagen City", "4060");
        Parkingspot new1 = new Parkingspot("Default", location, 19.99, 4);

        // act
        new1 = parkingspotRepository.add(new1);
        int size = parkingspotRepository.getParkingspotList().size();

        // assert
        Table locationTable = new Table(Datasource.getDataSource(), "parkingspot");
        output(locationTable).toConsole();
        org.assertj.db.api.Assertions.assertThat(locationTable).hasNumberOfRows(1);

        assertThat(parkingspotRepository.findById(new1.getId())).isNotNull();
        assertThat(parkingspotRepository.findById(new1.getId()).getType()).isEqualTo("Default");
        assertThat(new1.getId()).isNotNull();
        assertThat(size).isEqualTo(1);
    }

    @Test
    @Order(700)
    void testGetFree() {
        // arrange
        Location location = new Location("Garagen City", "4060");
        Customer customer = new Customer("Max", "Mustermann", true);

        Parkingspot new1 = new Parkingspot("Default", location, 19.99, 4);
        Parkingspot new2 = new Parkingspot("Default", location, 19.99, 4);
        Parkingspot new3 = new Parkingspot("Default", location, 19.99, 4);
        Parkingspot new4 = new Parkingspot("Default", location, 19.99, 4);
        Parkingspot new5 = new Parkingspot("Default", location, 19.99, 4);

        // act
        new1 = parkingspotRepository.add(new1);
        new2 = parkingspotRepository.add(new2);
        new3 = parkingspotRepository.add(new3);
        new4 = parkingspotRepository.add(new4);
        new5 = parkingspotRepository.add(new5);
        int size = parkingspotRepository.getParkingspotList().size();
        List<Parkingspot> free = parkingspotRepository.getFreeParkingspots(location);

        Contract contract = new Contract(customer, new1, LocalDate.now());
        contract = contractRepository.add(contract);
        List<Parkingspot> freeAfterRent = parkingspotRepository.getFreeParkingspots(location);

        // assert
        assertThat(new1.getId()).isNotNull();
        assertThat(new2.getId()).isNotNull();
        assertThat(new3.getId()).isNotNull();
        assertThat(new4.getId()).isNotNull();
        assertThat(new5.getId()).isNotNull();
        assertThat(contract.getId()).isNotNull();
        assertThat(size).isEqualTo(5);
        assertThat(free.size()).isEqualTo(5);
        assertThat(freeAfterRent.size()).isEqualTo(4);
    }

    @Test
    @Order(700)
    void testGetFreeButNoParkingspots() {
        // arrange
        Location location = new Location("Garagen City", "4060");

        // act
        location = locationRepository.add(location);
        int size = parkingspotRepository.getParkingspotList().size();
        List<Parkingspot> free = parkingspotRepository.getFreeParkingspots(location);

        // assert
        assertThat(size).isEqualTo(0);
        assertThat(free.size()).isEqualTo(0);
    }
}