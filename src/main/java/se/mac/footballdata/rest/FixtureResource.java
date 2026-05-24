package se.mac.footballdata.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/fixtures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FixtureResource {

    @GET
    public List<Fixture> all() {
        return Fixture.listAll();
    }

    @GET
    @Path("/{id}")
    public Fixture byId(@PathParam("id") String id) {
        Fixture lag = Fixture.findById(new ObjectId(id));
        if (lag == null) {
            throw new WebApplicationException("Hittades inte", Response.Status.NOT_FOUND);
        }
        return lag;
    }

}

