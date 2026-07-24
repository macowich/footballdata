package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.mac.footballdata.predictions.PredictionRepository;
import se.mac.footballdata.predictions.ResultPredictor;
import se.mac.footballdata.predictions.model.Prediction;
import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.Fixture;
import se.mac.footballdata.rest.model.Team;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static se.mac.footballdata.Util.createTeamFromEvent;
import static se.mac.footballdata.predictions.ResultPredictor.printResults;

@Path("/predictions")
@Produces(MediaType.APPLICATION_JSON)
public class PredictionResource {

    @Inject
    PredictionRepository predictionRepository;

    @Inject
    EventRepository eventRepository;

    @Inject
    FixtureRepository fixtureRepository;

    @GET
    @Path("/all")
    public List<Prediction> all() {
        return predictionRepository.listAll();
    }

    @GET
    @Path("/load")
    public Response load() {
        loadPredictions();
        return Response.ok("OK").build();
    }

    void loadPredictions() {
        String startDate = LocalDate.now().toString();
        List<Fixture> fixtureList = fixtureRepository.findByDate(startDate);
        for (Fixture f : fixtureList) {
            List<Event> homeTeamResult = eventRepository.findByTeam(f.hometeam);
            List<Event> awayTeamResult = eventRepository.findByTeam(f.awayteam);
            Team homeTeam = createTeamFromEvent(f.hometeam, homeTeamResult);
            Team awayTeam = createTeamFromEvent(f.awayteam, awayTeamResult);

            ResultPredictor resultPredictor = new ResultPredictor();

            try {
                // goalsHomeTeam, concededHomeTeam, goalsHomeLeague, concededHomeLeague,
                // goalsAwayTeam, concededAwayTeam, concededHomeLeague, goalsHomeLeague
                resultPredictor.calculateOutcomeProbability(homeTeam.goalsHomeTeam, homeTeam.concededHomeTeam, homeTeam.goalsHomeLeague,
                        homeTeam.concededHomeLeague,
                        awayTeam.goalsAwayTeam, awayTeam.concededAwayTeam, awayTeam.concededHomeLeague, awayTeam.goalsHomeLeague);

                printResults(0, 0, resultPredictor.getResultList(), resultPredictor.getHomeResultMap(),
                        resultPredictor.getAwayResultMap(), resultPredictor.getOddsInfoMap());

                Prediction p = createPrediction(f, resultPredictor.getOddsInfoMap());
                predictionRepository.saveOrOverwrite(p);
            } catch (Exception e) {
                System.out.println("Kunde inte skapa prediction för " + homeTeam.name + "-" + awayTeam.name);
            }
        }
    }

    static Prediction createPrediction(Fixture f, Map<String, Float> oddsInfoMap) {
        Prediction prediction = new Prediction();
        prediction.eventId = f.eventId;
        prediction.hometeam = f.hometeam;
        prediction.awayteam = f.awayteam;
        prediction.date = f.date;
        prediction.time = f.time;
        prediction.homeWin = String.valueOf(oddsInfoMap.get("1"));
        prediction.draw = String.valueOf(oddsInfoMap.get("x"));
        prediction.awayWin = String.valueOf(oddsInfoMap.get("2"));
        prediction.o25 = String.valueOf(oddsInfoMap.get("o2.5"));
        prediction.u25 = String.valueOf(oddsInfoMap.get("u2.5"));
        return prediction;
    }
}
