package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Fixture;

import java.util.List;

@ApplicationScoped
public class FixtureRepository implements PanacheMongoRepository<Fixture> {

    public List<Fixture> findByHomeTeam(String homeTeam) {
        return list("hometeam", homeTeam);
    }

    public List<Fixture> findByAwayTeam(String awayTeam) {
        return list("awayteam", awayTeam);
    }

    public List<Fixture> findByLeague(String league) {
        return list("league", league);
    }

    public List<Fixture> findByDate(String date) {
        return list("date", date);
    }
}
