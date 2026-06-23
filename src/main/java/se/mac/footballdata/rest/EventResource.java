package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import se.mac.footballdata.Util;
import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.League;
import se.mac.footballdata.rest.model.Odds;
import se.mac.footballdata.rest.model.Referee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    EventRepository eventRepository;

    @Inject
    RefereeRepository refereeRepository;

    @Inject
    OddsRepository oddsRepository;

    @GET
    @Path("/league/{league}")
    public List<Event> events(@PathParam("league") String league) {

        List<Event> eventList = eventRepository.findByLeague(Integer.parseInt(league));
        for (Event e: eventList) {
            Optional<Odds> o = oddsRepository.findByEventId(e.eventId);
            o.ifPresent(odds -> e.odds = odds);
        }

        return eventList;
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
        return new ArrayList<>(Util.leagues.values());
    }

    @GET
    @Path("/team/{team}")
    public List<Event> getEventsForTeam(@PathParam("team") String team) {
        return eventRepository.findByTeam(team);
    }

}
