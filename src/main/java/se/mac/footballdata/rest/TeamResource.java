package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import se.mac.footballdata.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {
    @Inject
    ResultRepository resultRepository;

    @GET
    @Path("/{league}")
    public List<Team> teamsByLeague(@PathParam("league") String league) {
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

    private Team createTeamFromResult(String name, List<Result> matches) {
        Team t = new Team();
        t.name = name;

        int homeGoals = 0;
        int homeGoalsConceeded = 0;
        int awayGoals = 0;
        int awayGoalsConceeded = 0;
        int homeMatches = 0;
        int awayMatches = 0;
        int over2Counter = 0;

        for (Result match : matches) {
            if (name.equals(match.homeTeam)) {
                homeGoals += match.fullTimeHomeGoals;
                homeGoalsConceeded += match.fullTimeAwayGoals;
                homeMatches++;
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                    over2Counter++;
                }
            } else if (name.equals(match.awayTeam)) {
                awayGoals += match.fullTimeAwayGoals;
                awayGoalsConceeded += match.fullTimeHomeGoals;
                awayMatches++;
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                    over2Counter++;
                }
            }
        }

        int totalGoals = homeGoals + awayGoals + homeGoalsConceeded + awayGoalsConceeded;

        double avgGoals =
                matches.isEmpty()
                        ? 0
                        : (double) totalGoals / (homeMatches + awayMatches);

        double avgHomeGoals =
                homeMatches == 0
                        ? 0
                        : (double) (homeGoals + homeGoalsConceeded) / homeMatches;

        double avgAwayGoals =
                awayMatches == 0
                        ? 0
                        : (double) (awayGoals + awayGoalsConceeded) / awayMatches;

        t.matcher = homeMatches + awayMatches;
        t.avgGoals = Util.round(avgGoals, 2);
        t.avgHomeGoals = Util.round(avgHomeGoals, 2);
        t.avgAwayGoals = Util.round(avgAwayGoals, 2);
        t.over2 = over2Counter;
        return t;
    }
}
