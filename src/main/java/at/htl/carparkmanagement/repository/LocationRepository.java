package at.htl.carparkmanagement.repository;

import at.htl.carparkmanagement.entity.Location;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class LocationRepository implements PanacheRepository<Location> {

    @Transactional
    public Location find(Long id) {
        try {
            return findById(id);
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Transactional
    public Location add(Location location) {
        if(location.getId() != null) {
            return null;
        }
        this.persist(location);
        return location;
    }

    @Transactional
    public Location update(Location location) {
        if(location.getId() != null) {
            Location update = this.find(location.getId());
            update.setName(location.getName());
            update.setZipcode(location.getZipcode());
            return update;
        }
        return this.add(location);
    }

    public List<Location> getLocationList() {
        return this.findAll().list();
    }

    @Transactional
    public boolean deleteLocation(Long id) {

        Location delete = this.find(id);
        if(delete == null) { return false; }
        this.delete(delete);
        return true;
    }
}
