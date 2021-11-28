package at.htl.carparkmanagement.boundary.parkingspot;

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
public class ParkingspotKarateRunner {
    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
    }

//    @Order(100)
//    @Karate.Test
//    @Transactional
//    Karate testPostParkingspot() {
//        entityManager.createQuery("DELETE FROM Parkingspot p").executeUpdate();
//        entityManager.createQuery("DELETE FROM Location l").executeUpdate();
//        return com.intuit.karate.junit5.Karate.run("parkingspot_post").relativeTo(getClass());
//    }
//
//    @Order(200)
//    @Karate.Test
//    Karate testGetParkingspot() {
//        return com.intuit.karate.junit5.Karate.run("parkingspot_get").relativeTo(getClass());
//    }
//
//    @Order(300)
//    @Karate.Test
//    Karate testDeleteParkingspot() {
//        return com.intuit.karate.junit5.Karate.run("parkingspot_delete").relativeTo(getClass());
//    }
//
//    @Order(400)
//    @Karate.Test
//    Karate testUpdateParkingspot() {
//        return com.intuit.karate.junit5.Karate.run("parkingspot_update").relativeTo(getClass());
//    }
}
