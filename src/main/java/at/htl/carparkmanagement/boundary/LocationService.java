package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.repository.LocationRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/api/location")
@Tag(name = "Location")
public class LocationService {
    @Inject
    LocationRepository locationRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Location> getAllLocations() {
        return locationRepository.findAll().list();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(@PathParam("id") Long id) {
        Location returnValue = locationRepository.find(id);
        if(returnValue == null) {
            return Response.noContent().build();
        }
        return Response.ok(returnValue).build();
    }

    @POST
    @Operation(
            summary = "Add Location",
            description = "For administrators to add Locations to a Collection in the Repository")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addLocation(Location newItem, @Context UriInfo uriInfo) {
        newItem = locationRepository.add(newItem);
        if(newItem == null) {
            return Response.notModified("resource already existent").build();
        }
        else {
            UriBuilder uriBuilder = uriInfo
                    .getAbsolutePathBuilder()
                    .path(newItem.getId().toString());
            return Response
                    .created(uriBuilder.build())
                    .build();
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateLocation(Location updated, @Context UriInfo uriInfo) {
        boolean added = false;
        if(updated.getId() == null) {
            added = true;
        }
        updated = locationRepository.update(updated);
        if(added) {
            UriBuilder uriBuilder = uriInfo
                    .getAbsolutePathBuilder()
                    .path(updated.getId().toString());
            return Response.created(uriBuilder.build()).build();
        }
        else {
            return Response.ok().build();
        }
    }

    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("{id}")
    public Response deleteLocation(@PathParam("id") Long id) {
        if(locationRepository.deleteLocation(id)) {
            return Response.ok("delete successful").build();
        }
        return Response.notModified("resource not found").build();
    }
}
