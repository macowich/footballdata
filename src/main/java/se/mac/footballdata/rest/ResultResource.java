package se.mac.footballdata.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResultResource {

    @GET
    public List<Result> results() {
        return Result.listAll();
    }

    // Hämta en specifik via ID
    @GET
    @Path("/{id}")
    public Result getById(@PathParam("id") String id) {
        Result result = Result.findById(new ObjectId(id));
        if (result == null) {
            throw new WebApplicationException("Hittades inte", Response.Status.NOT_FOUND);
        }
        return result;
    }
}
