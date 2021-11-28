package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Customer;
import at.htl.carparkmanagement.repository.CustomerRepository;
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
public class CustomerServiceTest {
    @Inject
    CustomerRepository customerRepository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM Contract c").executeUpdate();
        entityManager.createQuery("DELETE FROM Parkingspot p").executeUpdate();
        entityManager.createQuery("DELETE FROM Location l").executeUpdate();
        entityManager.createQuery("DELETE FROM Customer c").executeUpdate();
    }

    @Test
    @Order(100)
    public void testPost() {
        // arrange
        Customer toPost = new Customer("Max", "Mustermann", true);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/customer")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        // assert
        String id = location.substring(location.lastIndexOf('/') + 1);
        assertThat(location).isEqualTo("http://localhost:8081/api/customer/" + id);
    }

    @Test
    @Order(200)
    public void testPostAlreadyAdded() {
        // arrange
        Customer toPost = new Customer("Max", "Mustermann", true);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/customer")
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
                .post("/api/customer")
                .then()
                .statusCode(304)
                .extract()
                .header("ETag");

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/customer/" + id);
        assertThat(header).isEqualTo("\"resource already existent\"");
    }

    @Test
    @Order(300)
    public void testGetSingle() {
        // arrange
        Customer toPost = new Customer("Max", "Mustermann", true);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/customer")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/customer/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/customer/" + id);
        assertThat(response).isEqualTo("{\"firstname\":\"Max\",\"id\":20,\"isPrivat\":true,\"lastname\":\"Mustermann\"}");
    }

    @Test
    @Order(300)
    public void testGetMultiple() {
        // arrange
        List<Customer> list = new LinkedList<>();
        list.add(new Customer("Max", "Mustermann", true));
        list.add(new Customer("Hans", "Huber", true));
        list.add(new Customer("Franz", "Hauser", true));
        list.add(new Customer("Harald", "Hausberg", false));

        // act
        String id = "";
        for (Customer item : list) {
            String location = given()
                    .contentType("application/json")
                    .body(item)
                    .when()
                    .post("/api/customer")
                    .then()
                    .statusCode(201)
                    .extract()
                    .header("Location");
            id = location.substring(location.lastIndexOf('/') + 1);
            assertThat(location).isEqualTo("http://localhost:8081/api/customer/" + id);
        }
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/customer/")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        long lastId = Long.parseLong(id);
        assertThat(response).isEqualTo("[{\"firstname\":\"Max\",\"id\":21,\"isPrivat\":true,\"lastname\":\"Mustermann\"},{\"firstname\":\"Hans\",\"id\":22,\"isPrivat\":true,\"lastname\":\"Huber\"},{\"firstname\":\"Franz\",\"id\":23,\"isPrivat\":true,\"lastname\":\"Hauser\"},{\"firstname\":\"Harald\",\"id\":24,\"isPrivat\":false,\"lastname\":\"Hausberg\"}]");
    }

    @Test
    @Order(400)
    public void testDeleteWithContent() {
        // arrange
        Customer toPost = new Customer("Max", "Mustermann", true);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .post("/api/customer")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String body = given()
                .contentType("application/json")
                .when()
                .delete("/api/customer/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/customer/" + id);
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
                .delete("/api/customer/999")
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
        Customer toPost = new Customer("Max", "Mustermann", true);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/customer")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/customer/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // act
        assertThat(location).isEqualTo("http://localhost:8081/api/customer/" + id);
        assertThat(response).isEqualTo("{\"firstname\":\"Max\",\"id\":26,\"isPrivat\":true,\"lastname\":\"Mustermann\"}");
    }

    @Test
    @Order(700)
    public void testPutModify() {
        // arrange
        Customer toPost = new Customer("Max", "Mustermann", true);

        // act
        String location = given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/customer")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");
        toPost.setLastname("Musterfrau");
        String id = location.substring(location.lastIndexOf('/') + 1);
        toPost.setId(Long.parseLong(id));
        given()
                .contentType("application/json")
                .body(toPost)
                .when()
                .put("/api/customer")
                .then()
                .statusCode(200);
        String response = given()
                .contentType("application/json")
                .when()
                .get("/api/customer/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        // assert
        assertThat(location).isEqualTo("http://localhost:8081/api/customer/" + id);
        assertThat(response).isEqualTo("{\"firstname\":\"Max\",\"id\":27,\"isPrivat\":true,\"lastname\":\"Musterfrau\"}");
    }
}
