package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.Referee;
import se.mac.footballdata.rest.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static se.mac.footballdata.Util.createTeamFromEvent;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    EventRepository eventRepository;

    @GET
    @Path("/{league}")
    public List<Team> teamsByLeague(@PathParam("league") String league) {
        List<Event> results = eventRepository.findByLeague(Integer.parseInt(league));

        List<String> uniqueTeams = results.stream()
                .flatMap(r -> Stream.of(r.homeTeam, r.awayTeam))
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Team> teamList = new ArrayList<>();
        for (String t : uniqueTeams) {
            teamList.add(createTeamFromEvent(t, results));
        }

        return teamList;
    }

    @GET
    @Path("/id/{team}")
    public Team getById(@PathParam("team") String team) {
        List<Event> teamResult = eventRepository.findByTeam(team);
        return createTeamFromEvent(team, teamResult);
    }

}
