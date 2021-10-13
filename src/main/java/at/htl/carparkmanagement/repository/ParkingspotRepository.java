package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.entity.Parkingspot;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class ParkingspotRepository implements PanacheRepository<Parkingspot> {

    @Inject
    LocationRepository locationRepository;

    @Transactional
    public Parkingspot find(Long id) {
        try {
            return this.findById(id);
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Transactional
    public Parkingspot add(Parkingspot parkingspot) {
        if(parkingspot.getId() != null) {
            return null;
        }
        locationRepository.add(parkingspot.getLocation());
        this.persist(parkingspot);
        return parkingspot;
    }

    @Transactional
    public Parkingspot update(Parkingspot parkingspot) {
        if(parkingspot.getId() != null) {
            Parkingspot update = this.find(parkingspot.getId());
            update.setLocation(parkingspot.getLocation());
            locationRepository.update(parkingspot.getLocation());
            update.setType(parkingspot.getType());
            update.setPosition(parkingspot.getPosition());
            update.setPricePerDay(parkingspot.getPricePerDay());
            return update;
        }
        return this.add(parkingspot);
    }

    public List<Parkingspot> getParkingspotList() {
        return this.findAll().list();
    }

    public List<Parkingspot> getFreeParkingspots(Location location) {
        return getEntityManager().createNamedQuery("Parkingspot.getFree", Parkingspot.class).setParameter("location", location).getResultList();
    }

    @Transactional
    public boolean deleteParkingspot(Long id) {
        Parkingspot delete = this.find(id);
        if(delete == null) { return false; }
        this.delete(delete);
        return true;
    }
}
