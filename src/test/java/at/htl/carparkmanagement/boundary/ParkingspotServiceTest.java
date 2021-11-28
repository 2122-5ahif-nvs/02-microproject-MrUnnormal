package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.entity.Parkingspot;
import at.htl.carparkmanagement.repository.ParkingspotRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParkingspotServiceTest {
    @Inject
    ParkingspotRepository parkingspotRepository;

    @Inject
    EntityManager entityManager;

    private Location location1;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM Contract c").executeUpdate();
        entityManager.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        entityManager.createQuery("DELETE FROM Location l").executeUpdate();
        location1 = new Location("Garagen City", "4060");
        entityManager.persist(location1);

    }

    @Test
    @Order(100)
    public void testPost() {
        // arrange
        //Location location1 = new Location("Garagen City", "4060");
        Parkingspot toPost = new Parkingspot("Default", location1, 19.99, 4);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/parkingspot")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        // assert
        String id = location.substring(location.lastIndexOf('/') + 1);
        assertThat(location).isEqualTo("http://localhost:8081/api/parkingspot/" + id);
    }

    @Test
    @Order(200)
    public void testPostAlreadyAdded() {
        // arrange
        Parkingspot toPost = new Parkingspot("Default", location1, 19.99, 4);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/parkingspot")
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
                .post("/api/parkingspot")
                .then()
                .statusCode(304)
                .extract()
                .header("ETag");

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/parkingspot/" + id);
        assertThat(header).isEqualTo("\"resource already existent\"");
    }

    @Test
    @Order(200)
    public void testGetSingle() {
        // arrange
        Parkingspot toPost = new Parkingspot("Default", location1, 19.99, 4);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/parkingspot")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/parkingspot/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/parkingspot/" + id);
        assertThat(response).isEqualTo("{\"id\":" + id + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"}");
    }

    @Test
    @Order(300)
    public void testGetMultiple() {
        // arrange
        List<Parkingspot> list = new LinkedList<>();
        list.add(new Parkingspot("Default", location1, 19.99, 4));
        list.add(new Parkingspot("Default", location1, 19.99, 4));
        list.add(new Parkingspot("Default", location1, 19.99, 4));
        list.add(new Parkingspot("Default", location1, 19.99, 4));

        // act
        String id = "";
        for (Parkingspot item : list) {
            String location = given()
                    .contentType("application/json")
                    .body(item)
                    .when()
                    .post("/api/parkingspot")
                    .then()
                    .statusCode(201)
                    .extract()
                    .header("Location");
            id = location.substring(location.lastIndexOf('/') + 1);
            assertThat(location).isEqualTo("http://localhost:8081/api/parkingspot/" + id);
        }
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/parkingspot/")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        long lastId = Long.parseLong(id);
        assertThat(response).isEqualTo("[{\"id\":16,\"location\":{\"id\":30,\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"}," +
                "{\"id\":17,\"location\":{\"id\":30,\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"}," +
                "{\"id\":18,\"location\":{\"id\":30,\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"}," +
                "{\"id\":19,\"location\":{\"id\":30,\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"}]");
    }

    @Test
    @Order(400)
    public void testDeleteWithContent() {
        // arrange
        Parkingspot toPost = new Parkingspot("Default", location1, 19.99, 4);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/parkingspot")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        String body = given()
                .contentType("application/json")
                .when()
                .delete("/api/parkingspot/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/parkingspot/" + id);
        assertThat(body).isEqualTo("delete successful");
    }

    @Test
    @Order(500)
    public void testDeleteNoContent() {
        // arrange

        // act
        String header = given()
                .contentType("application/json")
                .when()
                .delete("/api/parkingspot/999")
                .then()
                .statusCode(304)
                .extract()
                .header("ETag");

        // assert
        assertThat(header).isEqualTo("\"resource not found\"");
    }

    @Test
    @Order(600)
    public void testPutCreate() {
        // arrange
        Parkingspot toPost = new Parkingspot("Default", location1, 19.99, 4);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/parkingspot")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/parkingspot/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/parkingspot/" + id);
        assertThat(response).isEqualTo("{\"id\":" + id + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"}");
    }

    @Test
    @Order(700)
    public void testPutModify() {
        // arrange
        Parkingspot toPost = new Parkingspot("Default", location1, 19.99, 4);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/parkingspot")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        location1.setName("Garagen-City");
        toPost.setLocation(location1);
        given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/parkingspot")
                .then()
                 .statusCode(200);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/parkingspot/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/parkingspot/" + id);
        assertThat(response).isEqualTo("{\"id\":" + id + ",\"location\":{\"id\":" + location1.getId() + ",\"name\":\"Garagen-City\",\"zipcode\":\"4060\"},\"position\":4,\"pricePerDay\":19.99,\"type\":\"Default\"}");
    }
}
