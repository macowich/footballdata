package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import se.mac.footballdata.rest.model.Fixture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/fixtures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FixtureResource {

    private static final DateTimeFormatter CSV_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Inject
    FixtureRepository repository;

    @GET
    @Path("/all")
    public List<Fixture> all() {
        return repository.listAll();
    }

    @GET
    public List<Fixture> getFixtures(@QueryParam("date") String date) {

        String searchDate;

        if (date == null || date.isBlank()) {
            searchDate = LocalDate.now().toString();
        } else {
            searchDate = date;
        }

        return repository.findByDate(searchDate);
    }

    @GET
    @Path("/team/{team}")
    public List<Fixture> teams(@PathParam("team") String team) {
        return repository.findByTeam(team);
    }

}

