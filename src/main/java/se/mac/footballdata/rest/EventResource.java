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
import se.mac.footballdata.rest.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.quarkus.mongodb.panache.PanacheMongoEntityBase.find;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    EventRepository eventRepository;

    @Inject
    IncidentsRepository incidentsRepository;

    @Inject
    RefereeRepository refereeRepository;

    @Inject
    OddsRepository oddsRepository;

    @Inject
    LineupRepository lineupRepository;

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
    public Event getById(@PathParam("id") String eventId) {
        Event event = eventRepository.findByEventId(Integer.parseInt(eventId));
        if (event == null) {
            throw new WebApplicationException("Event med id " + eventId + " hittades inte", Response.Status.NOT_FOUND);
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

    @GET
    @Path("/incidents/{eventId}")
    public List<Incident> getIncidents(@PathParam("eventId") String eventId) {
        return incidentsRepository.findByEventId(Integer.parseInt(eventId));
    }

    @GET
    @Path("/lineups/{eventId}")
    public Lineup getLineups(@PathParam("eventId") String eventId) {
        return lineupRepository.findByEventId(Integer.parseInt(eventId));
    }

}
