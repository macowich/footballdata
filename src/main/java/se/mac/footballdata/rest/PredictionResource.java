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
import se.mac.footballdata.rest.model.Fixture;
import se.mac.footballdata.rest.model.Result;
import se.mac.footballdata.rest.model.Team;

import java.util.List;
import java.util.Map;

import static se.mac.footballdata.Util.createTeamFromResult;
import static se.mac.footballdata.predictions.ResultPredictor.printResults;

@Path("/predictions")
@Produces(MediaType.APPLICATION_JSON)
public class PredictionResource {
    @Inject
    PredictionRepository predictionRepository;

    @Inject
    FixtureRepository fixtureRepository;

    @Inject
    ResultRepository resultRepository;

    @GET
    @Path("/all")
    public List<Prediction> all() {

        List<Prediction> predictions = predictionRepository.listAll();
        return predictions;
    }

    @GET
    @Path("/load")
    public Response load() {
        loadPredictions();
        return Response.ok("OK").build();
    }

    void loadPredictions() {
        List<Fixture> fixtureList = fixtureRepository.findByHomeTeam("Brighton");
        Fixture f = fixtureList.getFirst();
        if (f == null) return;

        List<Result> homeTeamResult = resultRepository.findByTeam("Brighton");
        List<Result> awayTeamResult = resultRepository.findByTeam("Man City");
        Team homeTeam = createTeamFromResult("Brighton", homeTeamResult);
        Team awayTeam = createTeamFromResult("Man City", awayTeamResult);

        ResultPredictor resultPredictor = new ResultPredictor();

        // goalsHomeTeam, concededHomeTeam, goalsHomeLeague, concededHomeLeague,
        // goalsAwayTeam, concededAwayTeam, concededHomeLeague, goalsHomeLeague
        resultPredictor.calculateOutcomeProbability(homeTeam.goalsHomeTeam, homeTeam.concededHomeTeam, homeTeam.goalsHomeLeague,
                homeTeam.concededHomeLeague,
                awayTeam.goalsAwayTeam, awayTeam.concededAwayTeam, awayTeam.concededHomeLeague, awayTeam.goalsHomeLeague);

        printResults(0, 0, resultPredictor.getResultList(), resultPredictor.getHomeResultMap(),
                resultPredictor.getAwayResultMap(), resultPredictor.getOddsInfoMap());

        Prediction p = createPrediction(f, resultPredictor.getOddsInfoMap());
        predictionRepository.saveOrOverwrite(p);
    }

    static Prediction createPrediction(Fixture f, Map<String, Float> oddsInfoMap) {
        Prediction prediction = new Prediction();
        prediction.fixture_id = f.fixture_id;
        prediction.hometeam = f.hometeam;
        prediction.awayteam = f.awayteam;
        prediction.date = f.date;
        prediction.time = f.time;
        prediction.homewin = String.valueOf(oddsInfoMap.get("1"));
        prediction.draw = String.valueOf(oddsInfoMap.get("x"));
        prediction.awaywin = String.valueOf(oddsInfoMap.get("2"));
        prediction.o25 = String.valueOf(oddsInfoMap.get("o2.5"));
        prediction.u25 = String.valueOf(oddsInfoMap.get("u2.5"));
        return prediction;
    }
}
