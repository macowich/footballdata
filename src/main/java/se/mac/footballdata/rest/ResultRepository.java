package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ResultRepository implements PanacheMongoRepository<Result> {

    public Result findByFixtureid(String fixtureId) {
        return find("fixture_id", fixtureId).singleResult();
    }

    public List<Result> findByTeam(String team) {
        List<Result> resultList = find(
                "{$or:[{hometeam:?1},{awayteam:?1}]}",
                team
        ).list();
        return resultList;
    }

    public List<Result> findByLeague(String league) {
        return find(
                "league",
                Sort.by("date").descending(),
                league
        ).list();
    }
}
