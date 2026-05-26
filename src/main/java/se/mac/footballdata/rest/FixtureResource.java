package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

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
        return Fixture.listAll();
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

}

