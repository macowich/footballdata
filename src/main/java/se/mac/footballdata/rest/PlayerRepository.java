package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Player;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlayerRepository implements PanacheMongoRepository<Player> {

    public Optional<Player> findByPlayerId(int playerId) {
        return find("playerId", playerId).singleResultOptional();
    }

    public List<Player> findByLeague(int leagueId) {
        return find(
                "leagueId",
                leagueId
        ).list();
    }

    public List<Player> findByTeam(String team) {
        return find(
                "teamName",
                team
        ).list();
    }
}
