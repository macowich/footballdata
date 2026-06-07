package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Event;

import java.util.List;

@ApplicationScoped
public class EventRepository implements PanacheMongoRepository<Event> {

    public List<Event> findByTeam(String team) {
        return find(
                "{$or:[{hometeam:?1},{awayteam:?1}]}",
                team
        ).list();
    }

    public List<Event> findByLeague(int leagueId) {
        return find(
                "leagueId",
                Sort.by("date").descending(),
                leagueId
        ).list();
    }
}
