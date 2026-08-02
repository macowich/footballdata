package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Fixture;

import java.util.List;

@ApplicationScoped
public class FixtureRepository implements PanacheMongoRepository<Fixture> {

    public List<Fixture> findByDate(String date) {
        return find("date >= ?1", Sort.by("date").and("time"), date).list();
    }

    public List<Fixture> findByTeamAndDate(String team, String date) {
        return find(
                "{$and:[" +
                        "{$or:[{hometeam:?1},{awayteam:?1}]}," +
                        "{date:{$gte:?2}}" +
                        "]}",
                Sort.by("date").descending(),
                team,
                date
        ).list();
    }


    public Fixture findByEventId(int eventId) {
        return find("eventId", eventId).singleResult();
    }
}
