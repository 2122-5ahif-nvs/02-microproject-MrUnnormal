package at.htl.carparkmanagement.boundary.location;

import com.intuit.karate.junit5.Karate;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocationKarateRunner {
    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
    }

//    @Order(100)
//    @Karate.Test
//    @Transactional
//    Karate testPostLocation() {
//        entityManager.createQuery("DELETE FROM Location l").executeUpdate();
//        return com.intuit.karate.junit5.Karate.run("location_post").relativeTo(getClass());
//    }
//
//    @Order(200)
//    @Karate.Test
//    Karate testGetLocation() {
//        return com.intuit.karate.junit5.Karate.run("location_get").relativeTo(getClass());
//    }
//
//    @Order(300)
//    @Karate.Test
//    Karate testDeleteLocation() {
//        return com.intuit.karate.junit5.Karate.run("location_delete").relativeTo(getClass());
//    }
//
//    @Order(400)
//    @Karate.Test
//    Karate testUpdateLocation() {
//        return com.intuit.karate.junit5.Karate.run("location_update").relativeTo(getClass());
//    }
}
