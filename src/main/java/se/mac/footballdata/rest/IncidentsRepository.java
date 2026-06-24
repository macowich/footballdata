package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import se.mac.footballdata.rest.model.Incident;
import se.mac.footballdata.rest.model.Odds;
import se.mac.footballdata.sportsapi.SportsApiClient;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class IncidentsRepository implements PanacheMongoRepository<Incident> {

    public List<Incident> findByEventId(int eventId) {

        return find("eventId", eventId).list();
    }

}
