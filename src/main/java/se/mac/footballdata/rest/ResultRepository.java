package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ResultRepository implements PanacheMongoRepository<Result> {

    public List<Result> findByTeam(String team) {
        return list("hometeam", team);
    }
}
