package at.htl.carparkmanagement.boundary;

import at.htl.carparkmanagement.entity.Customer;
import at.htl.carparkmanagement.repository.CustomerRepository;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("api/customer")
@Tag(name = "Customer")
public class CustomerService {
    @Inject
    CustomerRepository customerRepository;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll().list();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getById(@PathParam("id") Long id) {
        Customer returnValue = customerRepository.findById(id);
        if(returnValue == null) {
            return Response.noContent().build();
        }
        return Response.ok(returnValue).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addCustomer(Customer newItem, @Context UriInfo uriInfo) {
        newItem = customerRepository.add(newItem);
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
    public Response updateCustomer(Customer updated, @Context UriInfo uriInfo) {
        boolean added = false;
        if(updated.getId() == null) {
            added = true;
        }
        updated = customerRepository.update(updated);
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
    public Response deleteCustomer(@PathParam("id") Long id) {
        if(customerRepository.deleteCustomer(id)) {
            return Response.ok("delete successful").build();
        }
        return Response.notModified("resource not found").build();
    }
}
