package se.mac.footballdata.predictions;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import se.mac.footballdata.predictions.model.Prediction;
import se.mac.footballdata.rest.*;

import java.util.List;
import java.util.Map;

//@QuarkusMain
public class PredictionsLoader implements QuarkusApplication {

    @Inject
    PredictionRepository predictionRepository;

    @Inject
    FixtureRepository fixtureRepository;

    @Inject
    ResultRepository resultRepository;

    static Prediction createPrediction(Map<String, Float> oddsInfoMap) {
        Prediction prediction = new Prediction();
        prediction.homewin = String.valueOf(oddsInfoMap.get("1"));
        prediction.draw = String.valueOf(oddsInfoMap.get("x"));
        prediction.awaywin = String.valueOf(oddsInfoMap.get("2"));
        prediction.o25 = String.valueOf(oddsInfoMap.get("o2.5"));
        prediction.u25 = String.valueOf(oddsInfoMap.get("u2.5"));
        //System.out.println("o3.5: " + oddsInfoMap.get("o3.5"));
        //System.out.println("o3.5: " + oddsInfoMap.get("u3.5"));
        return prediction;
    }

    void loadPredictions() {
        List<Fixture> fixtureList = fixtureRepository.findByDate("2026-05-23");
        Fixture f = fixtureList.getFirst();
        List<Result> homeTeamResult = resultRepository.findByTeam("Liverpool");

        Team homeTeam = se.mac.footballdata.Util.createTeamFromResult("Liverpool", homeTeamResult, "home");

        ResultPredictor resultPredictor = new ResultPredictor();
        resultPredictor.calculateOutcomeProbability(homeTeam.avgHomeGoals, homeTeam.avgAwayGoals, homeTeam.goals, homeTeam.goalsconceeded, 1.26, 1.32, 1.21, 1.49);


    }

    @Override
    public int run(String... args) throws Exception {
        return 1;
    }
    /*
    @Override
    public int run(String... args) throws Exception {
        // Quarkus är nu startat och du kan använda ditt repository säkert!
        ResultPredictor resultPredictor = new ResultPredictor();
        resultPredictor.calculateOutcomeProbability(1.84, 0.79, 1.49, 1.21, 1.26, 1.32, 1.21, 1.49);

        Prediction p = createPrediction(resultPredictor.getOddsInfoMap());
        p.fixture_id = "A:B:C";
        predictionRepository.saveOrOverwrite(p);

        resultPredictor.calculateOutcomeProbability(3, 6, 1.49, 1.21, 1.26, 1.32, 1.21, 1.49);

         p = createPrediction(resultPredictor.getOddsInfoMap());
        p.fixture_id = "X:Y:C";
        predictionRepository.saveOrOverwrite(p);

        System.out.println("Prediction sparad framgångsrikt!");
        return 0; // Avslutar programmet normalt
    }

    public static void main(String... args) {
        Quarkus.run(PredictionsLoader.class, args);
    }

     */
}
