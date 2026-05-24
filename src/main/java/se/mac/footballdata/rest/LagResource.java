package se.mac.footballdata.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/lag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LagResource {

    // Hämta alla lagkombinationer
    @GET
    public List<LagKombination> listAll() {
        return LagKombination.listAll();
    }

    // Hämta en specifik via ID
    @GET
    @Path("/{id}")
    public LagKombination getById(@PathParam("id") String id) {
        LagKombination lag = LagKombination.findById(new ObjectId(id));
        if (lag == null) {
            throw new WebApplicationException("Hittades inte", Response.Status.NOT_FOUND);
        }
        return lag;
    }

    // Spara en ny kombination
    @POST
    public Response create(LagKombination lag) {
        lag.persist();
        return Response.status(Response.Status.CREATED).entity(lag).build();
    }

    // Ta bort via ID
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        boolean deleted = LagKombination.deleteById(new ObjectId(id));
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}

