package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Contract;
import at.htl.carparkmanagement.entity.Customer;
import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.entity.Parkingspot;
import at.htl.carparkmanagement.repository.ContractRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ContractServiceTest {
    @Inject
    ContractRepository contractRepository;

    @Inject
    EntityManager entityManager;

    Customer customer = null;
    Location location1 = null;
    Parkingspot parkingspot = null;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM Contract c").executeUpdate();
        entityManager.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        entityManager.createQuery("DELETE FROM Location l").executeUpdate();
        entityManager.createQuery("DELETE FROM Customer c").executeUpdate();
        customer = new Customer("Max", "Mustermann", true);
        location1 = new Location("Garagen City", "4060");
        parkingspot = new Parkingspot("Default", location1, 19.99, 4);
        entityManager.persist(customer);
        entityManager.persist(location1);
        entityManager.persist(parkingspot);
    }

    @Test
    public void testPost() {
        // arrange
        Contract toPost = new Contract(customer, parkingspot, LocalDate.now());

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/contract")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        // assert
        String id = location.substring(location.lastIndexOf('/') + 1);
        assertThat(location).isEqualTo("http://localhost:8081/api/contract/" + id);
    }

    @Test
    public void testPostAlreadyAdded() {
        // arrange
        Contract toPost = new Contract(customer, parkingspot, LocalDate.now());

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/contract")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        String header = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/contract")
                .then()
                .statusCode(304)
                .extract()
                .header("ETag");

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/contract/" + id);
        assertThat(header).isEqualTo("\"resource already existent\"");
    }

    @Test
    public void testGetSingle() {
        // arrange
        Contract toPost = new Contract(customer, parkingspot, LocalDate.now());

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/contract")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/contract/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/contract/" + id);
        assertThat(response).isEqualTo("{\"customer\":{\"firstname\":\"Max\",\"id\":" + customer.getId() + ",\"isPrivat\":true,\"lastname\":\"Mustermann\"}," +
                "\"id\":" + id + ",\"parkingspot\":{\"id\":" + parkingspot.getId() + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"}," +
                "\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"},\"startDate\":\"" + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now()) + "\"}");
    }

    @Test
    public void testGetMultiple() {
        // arrange
        List<Contract> list = new LinkedList<>();
        list.add(new Contract(customer, parkingspot, LocalDate.now()));
        list.add(new Contract(customer, parkingspot, LocalDate.now()));
        list.add(new Contract(customer, parkingspot, LocalDate.now()));
        list.add(new Contract(customer, parkingspot, LocalDate.now()));

        // act
        String id = "";
        for (Contract item : list) {
            String location = given()
                    .contentType("application/json")
                    .body(item)
                    .when()
                    .post("/api/contract")
                    .then()
                    .statusCode(201)
                    .extract()
                    .header("Location");
            id = location.substring(location.lastIndexOf('/') + 1);
            assertThat(location).isEqualTo("http://localhost:8081/api/contract/" + id);
        }
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/contract/")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        long lastId = Long.parseLong(id);
        assertThat(response).isEqualTo("[{\"customer\":{\"firstname\":\"Max\",\"id\":" + customer.getId() + ",\"isPrivat\":true,\"lastname\":\"Mustermann\"},\"id\":" + (lastId - 3) + ",\"parkingspot\":{\"id\":" + parkingspot.getId() + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"},\"startDate\":\""+ DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now()) + "\"}," +
                "{\"customer\":{\"firstname\":\"Max\",\"id\":" + customer.getId() + ",\"isPrivat\":true,\"lastname\":\"Mustermann\"},\"id\":" + (lastId - 2) + ",\"parkingspot\":{\"id\":" + parkingspot.getId() + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"},\"startDate\":\"" + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now()) + "\"}," +
                "{\"customer\":{\"firstname\":\"Max\",\"id\":" + customer.getId() + ",\"isPrivat\":true,\"lastname\":\"Mustermann\"},\"id\":" + (lastId - 1) + ",\"parkingspot\":{\"id\":" + parkingspot.getId() + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"},\"startDate\":\"" + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now()) + "\"}," +
                "{\"customer\":{\"firstname\":\"Max\",\"id\":" + customer.getId() + ",\"isPrivat\":true,\"lastname\":\"Mustermann\"},\"id\":" + lastId + ",\"parkingspot\":{\"id\":" + parkingspot.getId() + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"},\"startDate\":\"" + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now()) + "\"}]");
    }

    @Test
    public void testDeleteWithContent() {
        // arrange
        Contract toPost = new Contract(customer, parkingspot, LocalDate.now());

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/contract")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String body = given()
                .contentType("application/json")
                .when()
                .delete("/api/contract/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/contract/" + id);
        assertThat(body).isEqualTo("delete successful");
    }

    @Test
    public void testDeleteNoContent() {
        // arrange

        // act
        String header = given()
                .contentType("application/json")
                .when()
                .delete("/api/contract/999")
                .then()
                .statusCode(304)
                .extract()
                .header("ETag");

        // assert
        assertThat(header).isEqualTo("\"resource not found\"");
    }

    @Test
    public void testPutCreate() {
        // arrange
        Contract toPost = new Contract(customer, parkingspot, LocalDate.now());

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/contract")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/contract/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/contract/" + id);
        assertThat(response).isEqualTo("{\"customer\":{\"firstname\":\"Max\",\"id\":14,\"isPrivat\":true,\"lastname\":\"Mustermann\"}," +
                "\"id\":8,\"parkingspot\":{\"id\":9,\"location\":{\"id\":13,\"name\":\"Garagen City\",\"zipcode\":\"4060\"}," +
                "\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"},\"startDate\":\"" + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now()) +  "\"}");
    }

    @Test
    public void testPutModify() {
        // arrange
        Contract toPost = new Contract(customer, parkingspot, LocalDate.now());

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/contract")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        toPost.setEndDate(LocalDate.now().plusMonths(5));
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/contract")
                .then()
                .statusCode(200);

        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/contract/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/contract/" + id);
        assertThat(response).isEqualTo("{\"customer\":{\"firstname\":\"Max\",\"id\":" + customer.getId() + ",\"isPrivat\":true,\"lastname\":\"Mustermann\"},\"endDate\":\"" + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now().plusMonths(5)) + "\"," +
                "\"id\":" + id + ",\"parkingspot\":{\"id\":" + parkingspot.getId() + ",\"location\":{\"id\":"+ location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"}," +
                "\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"},\"startDate\":\"" + DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.now()) + "\"}");
    }
}
