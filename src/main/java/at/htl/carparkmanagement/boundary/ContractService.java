package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Contract;
import at.htl.carparkmanagement.repository.ContractRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("api/contract")
@Tag(name = "Contract")
public class ContractService {
    @Inject
    ContractRepository contractRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Contract> getAllContract() {
        return contractRepository.findAll().list();
    }

    @GET
    @Path("{id}")
    @Operation(description = "Returns the Contract with the specified ID")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(@PathParam("id") Long id) {
        Contract returnValue = contractRepository.find(id);
        if(returnValue == null) {
            return Response.noContent().build();
        }
        return Response.ok(returnValue).build();
    }

    @POST
    @Operation(summary = "Rent Parkingspot", description = "Adds a Contract with all its nested objects (Customer, Parkingspot, Location) to a collection in the Repository when a customer rents a Parkingspot")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addContract(Contract newItem, @Context UriInfo uriInfo) {
        newItem = contractRepository.add(newItem);
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
    public Response updateContract(Contract updated, @Context UriInfo uriInfo) {
        boolean added = false;
        if(updated.getId() == null) {
            added = true;
        }
        updated = contractRepository.update(updated);
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
    public Response deleteContract(@PathParam("id") Long id) {
        if(contractRepository.deleteContract(id)) {
            return Response.ok("delete successful").build();
        }
        return Response.notModified("resource not found").build();
    }
}
