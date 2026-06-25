package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Lineup;

@ApplicationScoped
public class LineupRepository implements PanacheMongoRepository<Lineup> {

    public Lineup findByEventId(int eventId) {
        return find("eventId", eventId).firstResult();
    }
}
