package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Location;
import at.htl.carparkmanagement.entity.Parkingspot;
import at.htl.carparkmanagement.repository.LocationRepository;
import at.htl.carparkmanagement.repository.ParkingspotRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

// patch um nur teile zu ändern
// put um ganzen datensatz zu ändern
// -> https://2021-4ahif-nvs.github.io/2021-4ahif-nvs-lecture-notes/
// swagger -> https://quarkus.io/guides/openapi-swaggerui

@Path("/api/parkingspot")
@Tag(name = "Parkingspot")
public class ParkingspotService {
    @Inject
    ParkingspotRepository parkingspotRepository;

    @Inject
    LocationRepository locationRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public JsonArray getAllParkingspots() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Parkingspot item : parkingspotRepository.findAll().list()) {
            JsonObject object = Json.createObjectBuilder()
                    .add("id", item.getId().toString())
                    .add("location", item.getLocation().toString())
                    .add("type", item.getType()).build();
            arrayBuilder.add(object);
        }
        return arrayBuilder.build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(@PathParam("id") Long id) {
        Parkingspot returnValue = parkingspotRepository.find(id);
        if(returnValue == null) {
            return Response.noContent().build();
        }
        return Response.ok(returnValue).build();
    }

    @GET
    @Operation(summary = "Get free spots", description = "Fetches all free parkingspots for the given location")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("free/{id}")
    public Response getFreeForLocation(@PathParam("id") Long locationId) {
        Location location = locationRepository.find(locationId);
        if(location == null) {
            return Response.noContent().tag("location not found").build();
        }
        List<Parkingspot> free = parkingspotRepository.getFreeParkingspots(location);
        if(free.size() == 0) {
            return Response.noContent().tag("no free parkingspots").build();
        }
        return Response.ok(free).build();
    }

    @POST
    @Operation(summary = "Add Parkingspot to Location", description = "For administrators to add a Parkingspot to a Collection in the Repository")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addParkingspot(Parkingspot newItem, @Context UriInfo uriInfo) {
        newItem = parkingspotRepository.add(newItem);
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
    public Response updateParkingspot(Parkingspot updated, @Context UriInfo uriInfo) {
        boolean added = false;
        if(updated.getId() == null) {
            added = true;
        }
        updated = parkingspotRepository.update(updated);
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
    public Response deleteParkingspot(@PathParam("id") Long id) {
        if(parkingspotRepository.deleteParkingspot(id)) {
            return Response.ok("delete successful").build();
        }
        return Response.notModified("resource not found").build();
    }
}
