package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import se.mac.footballdata.rest.model.Referee;

import java.util.List;

@Path("/referees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RefereeResource {

    @Inject
    RefereeRepository refereeRepository;

    @GET
    @Path("/league/{league}")
    public List<Referee> referees(@PathParam("league") int league) {
        return refereeRepository.findByLeague(league);
    }

    @GET
    @Path("/{id}")
    public Referee getById(@PathParam("id") String id) {
        Referee referee = Referee.findById(new ObjectId(id));
        if (referee == null) {
            throw new WebApplicationException("Referee med id " + id + " hittades inte", Response.Status.NOT_FOUND);
        }
        return referee;
    }

}
