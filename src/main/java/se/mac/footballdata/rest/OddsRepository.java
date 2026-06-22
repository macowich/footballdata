package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Odds;

import java.util.Optional;

@ApplicationScoped
public class OddsRepository implements PanacheMongoRepository<Odds> {

    public Optional<Odds> findByEventId(int eventId) {
        return find("eventId", eventId).singleResultOptional();
    }

}
