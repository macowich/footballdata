package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.Player;
import se.mac.footballdata.rest.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static se.mac.footballdata.Util.createTeamFromEvent;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerRepository playerRepository;

    @GET
    @Path("/{league}")
    public List<Player> playersByLeague(@PathParam("league") String league) {
        return playerRepository.findByLeague(Integer.parseInt(league));
    }

    @GET
    @Path("/team/{team}")
    public List<Player> playersByTeam(@PathParam("team") String team) {
        return playerRepository.findByTeam(team);
    }

    /*
    @GET
    @Path("/id/{id}")
    public Team getById(@PathParam("player") int playerId) {
        Optional<Player> player = playerRepository.findByPlayerId(playerId);
        return player.get();
    }
*/
}
