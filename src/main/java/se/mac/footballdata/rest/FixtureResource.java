package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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

        List<Fixture> fixtures = repository.listAll();

        fixtures.forEach(f ->
                f.league = getLeagueName(f.league)
        );

        return fixtures;
    }

    @GET
    public List<Fixture> getFixtures(@QueryParam("date") String date) {

        String searchDate;

        if (date == null || date.isBlank()) {
            searchDate = LocalDate.now().format(CSV_DATE_FORMAT);
        } else {
            searchDate = date;
        }

        return repository.findByDate(searchDate);
    }

    private String getLeagueName(String code) {
        return switch (code) {
            case "B1" -> "Belgium First Division";
            case "E0" -> "Premier League";
            case "SP1" -> "La Liga";
            case "SP2" -> "Secunda divison";
            case "D1" -> "Bundesliga";
            case "I1" -> "Serie A";
            case "F1" -> "Ligue 1";
            default -> code;
        };
    }

}

