package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.repository.LocationRepository;
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
public class LocationServiceTest {
    @Inject
    LocationRepository locationRepository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM Contract c").executeUpdate();
        entityManager.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        entityManager.createQuery("DELETE FROM Location l").executeUpdate();
    }

    @Test
    @Order(100)
    public void testPost() {
        // arrange
        Location toPost = new Location("Garagen City", "4060");

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        // assert
        String id = location.substring(location.lastIndexOf('/') + 1);
        assertThat(location).isEqualTo("http://localhost:8081/api/location/" + id);
    }

    @Test
    @Order(200)
    public void testPostAlreadyAdded() {
        // arrange
        Location toPost = new Location("Garagen City", "4060");

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/location")
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
                .post("/api/location")
                .then()
                .statusCode(304)
                .extract()
                .header("ETag");

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/location/" + id);
        assertThat(header).isEqualTo("\"resource already existent\"");
    }

    @Test
    @Order(300)
    public void testGetSingle() {
        // arrange
        Location toPost = new Location("Garagen City", "4060");

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/location/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/location/" + id);
        assertThat(response).isEqualTo("{\"id\":" + id + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"}");
    }

    @Test
    @Order(400)
    public void testGetMultiple() {
        // arrange
        List<Location> list = new LinkedList<>();
        list.add(new Location("Garagen City Linz", "4060"));
        list.add(new Location("Garagen City Irgendwo1", "5360"));
        list.add(new Location("Garagen City Irgendwo2", "45012"));
        list.add(new Location("Garagen City Irgendwo3", "6506"));

        // act
        String id = "";
        for (Location item : list) {
            String location = given()
                    .contentType("application/json")
                    .body(item)
                    .when()
                    .post("/api/location")
                    .then()
                    .statusCode(201)
                    .extract()
                    .header("Location");
            id = location.substring(location.lastIndexOf('/') + 1);
            assertThat(location).isEqualTo("http://localhost:8081/api/location/" + id);
        }
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/location/")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        long lastId = Long.parseLong(id);
        assertThat(response).isEqualTo("[{\"id\":" + (lastId - 3) + ",\"name\":\"Garagen City Linz\",\"zipcode\":\"4060\"}," +
                "{\"id\":" + (lastId - 2) + ",\"name\":\"Garagen City Irgendwo1\",\"zipcode\":\"5360\"}," +
                "{\"id\":" + (lastId - 1) + ",\"name\":\"Garagen City Irgendwo2\",\"zipcode\":\"45012\"}," +
                "{\"id\":" + lastId + ",\"name\":\"Garagen City Irgendwo3\",\"zipcode\":\"6506\"}]");
    }

    @Test
    @Order(500)
    public void testDeleteWithContent() {
        // arrange
        Location toPost = new Location("Garagen City", "4060");

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String body = given()
                .contentType("application/json")
                .when()
                .delete("/api/location/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/location/" + id);
        assertThat(body).isEqualTo("delete successful");
    }

    @Test
    @Order(600)
    public void testDeleteNoContent() {
        // arrange

        // act
        String header = given()
                .contentType("application/json")
                .when()
                .delete("/api/location/999")
                .then()
                .statusCode(304)
                .extract()
                .header("ETag");

        // assert
        assertThat(header).isEqualTo("\"resource not found\"");
    }

    @Test
    @Order(700)
    public void testPutCreate() {
        // arrange
        Location toPost = new Location("Garagen City", "4060");

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/location/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/location/" + id);
        assertThat(response).isEqualTo("{\"id\":" + id + ",\"name\":\"Garagen City\",\"zipcode\":\"4060\"}");
    }

    @Test
    @Order(800)
    public void testPutModify() {
        // arrange
        Location toPost = new Location("Garagen City", "4060");

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/location")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        toPost.setName("Garagen-City");
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/location")
                .then()
                .statusCode(200);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/location/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/location/" + id);
        assertThat(response).isEqualTo("{\"id\":" + id + ",\"name\":\"Garagen-City\",\"zipcode\":\"4060\"}");
    }
}
