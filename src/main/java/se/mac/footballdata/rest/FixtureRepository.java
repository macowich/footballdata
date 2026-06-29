package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Fixture;
import se.mac.footballdata.rest.model.Lineup;

import java.util.List;

@ApplicationScoped
public class FixtureRepository implements PanacheMongoRepository<Fixture> {

    public List<Fixture> findByDate(String date) {
        return list("date", date);
    }

    public List<Fixture> findByTeam(String team) {
        return find(
                "{$or:[{hometeam:?1},{awayteam:?1}]}",
                Sort.by("date").descending(),
                team
        ).list();
    }

    public Fixture findByEventId(int eventId) {
        return find("eventId", eventId).singleResult();
    }
}
