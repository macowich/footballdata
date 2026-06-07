package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import se.mac.footballdata.rest.model.Result;
import se.mac.footballdata.rest.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static se.mac.footballdata.Util.createTeamFromResult;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {
    @Inject
    ResultRepository resultRepository;

    @GET
    @Path("/{league}")
    public List<Team> teamsByLeague(@PathParam("league") String league, @QueryParam("filter") String filter) {
        List<Result> results = resultRepository.findByLeague(league);

        List<String> uniqueTeams = results.stream()
                .flatMap(r -> Stream.of(r.homeTeam, r.awayTeam))
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<Team> teamList = new ArrayList<>();
        for (String t : uniqueTeams) {
            teamList.add(createTeamFromResult(t, results));
        }

        return teamList;
    }


}
