package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FixtureRepository implements PanacheMongoRepository<Fixture> {

    public List<Fixture> findByHomeTeam(String homeTeam) {
        return list("HomeTeam", homeTeam);
    }

    public List<Fixture> findByAwayTeam(String awayTeam) {
        return list("AwayTeam", awayTeam);
    }

    public List<Fixture> findByDivision(String div) {
        return list("Div", div);
    }

    public List<Fixture> findByDate(String date) {
        return list("Date", date);
    }
}
