package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.Referee;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EventRepository implements PanacheMongoRepository<Event> {

    public Event findByEventId(int eventId) {
        return find("eventId", eventId).singleResult();
    }

    public List<Event> findByTeam(String team) {
        return find(
                "{$or:[{hometeam:?1},{awayteam:?1}]}",
                Sort.by("date").descending(),
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
