package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Referee;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RefereeRepository implements PanacheMongoRepository<Referee> {

    public Optional<Referee> findByRefereeId(int refereeId) {
        return find("refereeId", refereeId).singleResultOptional();
    }

    public List<Referee> findByLeague(int leagueId) {
        return find(
                "leagueId",
                Sort.by("date").descending(),
                leagueId
        ).list();
    }
}
