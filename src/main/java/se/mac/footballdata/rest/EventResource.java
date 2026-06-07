package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.League;
import se.mac.footballdata.rest.model.Referee;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    static List<League> leagues =
            Arrays.asList(new League(1, "Premier League"), new League(26, "Allsvenskan"));

    @Inject
    EventRepository eventRepository;

    @Inject
    RefereeRepository refereeRepository;

    @GET
    @Path("/league/{league}")
    public List<Event> events(@PathParam("league") String league) {
        return eventRepository.findByLeague(Integer.parseInt(league));
    }

    @GET
    @Path("/{id}")
    public Event getById(@PathParam("id") String id) {
        Event event = Event.findById(new ObjectId(id));
        if (event == null) {
            throw new WebApplicationException("Event med id " + id + " hittades inte", Response.Status.NOT_FOUND);
        }

        Optional<Referee> r = refereeRepository.findByRefereeId(event.refereeId);
        r.ifPresent(referee -> event.referee = referee.name);

        return event;
    }

    @GET
    @Path("/league/all")
    public List<League> leagues() {
        return leagues;
    }

}
