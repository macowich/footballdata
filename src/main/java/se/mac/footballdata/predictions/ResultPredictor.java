package se.mac.footballdata.predictions;

import java.math.BigDecimal;
import java.util.*;

public class ResultPredictor {

    private static int noOfSamples = 100000;

    private HashMap<String, Float> homeResultMap;
    private HashMap<String, Float> awayResultMap;
    private List<Result> resultList = new ArrayList<Result>();

    private Map<String, Float> oddsInfoMap = new HashMap<String, Float>();

    public static void main(String[] args) throws Exception {
        System.out.println("ResultPredictor started");

        ResultPredictor resultPredictor = new ResultPredictor();
        resultPredictor.calculateOutcomeProbability(1.84, 0.79, 1.49, 1.21, 1.26, 1.32, 1.21, 1.49);

        printResults(0, 0, resultPredictor.getResultList(), resultPredictor.getHomeResultMap(),
                resultPredictor.getAwayResultMap(), resultPredictor.getOddsInfoMap());

        System.out.println("\n");
		/*resultPredictor = new ResultPredictor();
		// Hometeam avg goals, awayteam avg goals
		resultPredictor.calculateOverUnderProbability(calculateMean(0.89, 1.33), calculateMean(0.56, 2.56));
		
		printResults(calculateMean(0.89, 1.33), calculateMean(0.56, 2.56), resultPredictor.getResultList(), resultPredictor.getHomeResultMap(),
				resultPredictor.getAwayResultMap(), resultPredictor.getOddsInfoMap());*/
    }

    public HashMap<String, Float> getHomeResultMap() {
        return homeResultMap;
    }

    public HashMap<String, Float> getAwayResultMap() {
        return awayResultMap;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public Map<String, Float> getOddsInfoMap() {
        return oddsInfoMap;
    }

    public void calculateOverUnderProbability(double hAvg, double bAvg) {
        homeResultMap = calculateScoreProbability(hAvg);
        awayResultMap = calculateScoreProbability(bAvg);

        calculateResults();
    }

    public void calculateOutcomeProbability(AvgGoalInfo avgGoalInfo) {
        calculateOutcomeProbability(avgGoalInfo.homegoals_team, avgGoalInfo.homeconceded_team, avgGoalInfo.homegoals_total,
                avgGoalInfo.homeconceded_total, avgGoalInfo.awaygoals_team, avgGoalInfo.awayconceded_team, avgGoalInfo.awaygoals_total,
                avgGoalInfo.awayconceded_total);
    }

    public void calculateOutcomeProbability(double homegoals_team, double homeconceded_team, double homegoals_total,
                                            double homeconceded_total, double awaygoals_team, double awayconceded_team, double awaygoals_total,
                                            double awayconceded_total) {

        double attackStrength = calculateStrength(homegoals_team, homegoals_total); // homegoals/team,
        // homegoals/total
        double defenceStrength = calculateStrength(awayconceded_team, awayconceded_total); // awayconceded/team,
        // awayconceded/total

        double homeScoreProb = attackStrength * defenceStrength * homegoals_total;

        attackStrength = calculateStrength(awaygoals_team, awaygoals_total); // awaygoals/team,
        // awaygoals/total
        defenceStrength = calculateStrength(homeconceded_team, homeconceded_total); // homeconceded/team,
        // homeconceded/total

        double awayScoreProb = attackStrength * defenceStrength * awaygoals_total;

        homeResultMap = calculateScoreProbability(homeScoreProb);
        awayResultMap = calculateScoreProbability(awayScoreProb);

        calculateResults();
    }

    private void calculateResults() {

        float underProb = 0;
        float homeProb = 0;
        float drawProb = 0;
        float awayProb = 0;
        float bttsYes = 0;
        float bttsNo = 0;
        float homeOver_1_5 = 0;
        float awayOver_1_5 = 0;

        float probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "0", "0");
        Result result = new Result(0, 0, probTmp);
        resultList.add(result);
        underProb += probTmp;
        drawProb += probTmp;
        bttsNo += probTmp;

        float uProb = round(underProb, 2);
        float oProb = 100 - uProb;
        oddsInfoMap.put("o0.5", getDecimalOdds(oProb));
        oddsInfoMap.put("u0.5", getDecimalOdds(uProb));

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "1", "0");
        result = new Result(1, 0, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsNo += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "0", "1");
        result = new Result(0, 1, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsNo += probTmp;

        uProb = round(underProb, 2);
        oProb = 100 - uProb;
        oddsInfoMap.put("o1.5", getDecimalOdds(oProb));
        oddsInfoMap.put("u1.5", getDecimalOdds(uProb));

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "1", "1");
        result = new Result(1, 1, probTmp);
        resultList.add(result);
        underProb += probTmp;
        drawProb += probTmp;
        bttsYes += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "2", "0");
        result = new Result(2, 0, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsNo += probTmp;
        homeOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "0", "2");
        result = new Result(0, 2, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsNo += probTmp;
        awayOver_1_5 += probTmp;

        uProb = round(underProb, 2);
        oProb = 100 - uProb;
        oddsInfoMap.put("o2.5", getDecimalOdds(oProb));
        oddsInfoMap.put("u2.5", getDecimalOdds(uProb));

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "2", "1");
        result = new Result(2, 1, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "1", "2");
        result = new Result(1, 2, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsYes += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "3", "0");
        result = new Result(3, 0, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsNo += probTmp;
        homeOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "0", "3");
        result = new Result(0, 3, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsNo += probTmp;
        awayOver_1_5 += probTmp;

        uProb = round(underProb, 2);
        oProb = 100 - uProb;
        oddsInfoMap.put("o3.5", getDecimalOdds(oProb));
        oddsInfoMap.put("u3.5", getDecimalOdds(uProb));

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "2", "2");
        result = new Result(2, 2, probTmp);
        resultList.add(result);
        underProb += probTmp;
        drawProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "3", "1");
        result = new Result(3, 1, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "3", "2");
        result = new Result(3, 2, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "1", "3");
        result = new Result(1, 3, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsYes += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "2", "3");
        result = new Result(2, 3, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "3", "3");
        result = new Result(3, 3, probTmp);
        resultList.add(result);
        underProb += probTmp;
        drawProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "4 or more", "0");
        result = new Result(4, 0, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsNo += probTmp;
        homeOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "0", "4 or more");
        result = new Result(0, 4, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsNo += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "1", "4 or more");
        result = new Result(1, 4, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsYes += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "2", "4 or more");
        result = new Result(2, 4, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "3", "4 or more");
        result = new Result(3, 4, probTmp);
        resultList.add(result);
        underProb += probTmp;
        awayProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "4 or more", "1");
        result = new Result(4, 1, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "4 or more", "2");
        result = new Result(4, 2, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "4 or more", "3");
        result = new Result(4, 3, probTmp);
        resultList.add(result);
        underProb += probTmp;
        homeProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        probTmp = getProbabilityForScore(homeResultMap, awayResultMap, "4 or more", "4 or more");
        result = new Result(4, 4, probTmp);
        resultList.add(result);
        underProb += probTmp;
        drawProb += probTmp;
        bttsYes += probTmp;
        homeOver_1_5 += probTmp;
        awayOver_1_5 += probTmp;

        oddsInfoMap.put("1", getDecimalOdds(homeProb));
        oddsInfoMap.put("x", getDecimalOdds(drawProb));
        oddsInfoMap.put("2", getDecimalOdds(awayProb));
        oddsInfoMap.put("bttsYes", getDecimalOdds(bttsYes));
        oddsInfoMap.put("bttsNo", getDecimalOdds(bttsNo));
        oddsInfoMap.put("homeOver_1_5", getDecimalOdds(homeOver_1_5));
        float homeUnder = 100 - homeOver_1_5;
        oddsInfoMap.put("homeUnder_1_5", getDecimalOdds(homeUnder));
        oddsInfoMap.put("awayOver_1_5", getDecimalOdds(awayOver_1_5));
    }

    private static double calculateStrength(double teamGoals, double avgGoals) {
        return teamGoals / avgGoals;
    }

    public static float getProbabilityForScore(HashMap<String, Float> hResultMap, HashMap<String, Float> bResultMap,
                                               String hScore, String bScore) {

        float hProb = hResultMap.get(hScore) / 100;
        float bProb = bResultMap.get(bScore) / 100;

        float avgProb = (hProb * bProb) * 100;

        return round(avgProb, 2);
    }

    private static HashMap<String, Float> calculateScoreProbability(double mean) {
        HashMap<String, Integer> score = new HashMap<String, Integer>();

        for (int i = 0; i < noOfSamples; i++) {
            int res = getPoissonRandom(mean);
            if (res == 0) {
                handleMap(score, "0");
            } else if (res == 1) {
                handleMap(score, "1");
            } else if (res == 2) {
                handleMap(score, "2");
            } else if (res == 3) {
                handleMap(score, "3");
            } else {
                handleMap(score, "4 or more");
            }
        }

        // System.out.println("\n" + score.toString());
        HashMap<String, Float> resultMap = new HashMap<String, Float>();

        for (String key : score.keySet()) {
            Integer no = score.get(key);
            float percent = (float) no / noOfSamples * 100;
            percent = round(percent, 2);
            resultMap.put(key, percent);
        }

        return resultMap;
    }

    private static void handleMap(HashMap<String, Integer> score, String noStr) {
        Integer no = score.get(noStr);
        if (no == null) {
            no = 1;
        } else {
            no++;
        }
        score.put(noStr, no);
    }

    private static float getDecimalOdds(float prob) {
        return round((100 / prob), 2);
    }

    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private static int getPoissonRandom(double mean) {
        Random r = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }

    public static double calculateMean(double d1, double d2) {
        double tmp = (d1 + d2) / 2;
        return tmp;
    }

    public static void printResults(double hAvg, double bAvg, List<Result> resultList,
                                    HashMap<String, Float> hResultMap, HashMap<String, Float> bResultMap, Map<String, Float> oddsInfoMap) {

        System.out.println("Goals\t\t|0\t|1\t|2\t|3\t|4 or more");
        System.out.println("---------------------------------------------------------");
        System.out.print("Hemmalag = " + hAvg + "\t");

        for (String key : hResultMap.keySet()) {
            System.out.print("|" + hResultMap.get(key) + "\t");
        }

        System.out.print("\nBortalag = " + bAvg + "\t");

        for (String key : bResultMap.keySet()) {
            System.out.print("|" + bResultMap.get(key) + "\t");
        }

        System.out.println("\n\nScore\t\t|Probability\t|O1.5\tU1.5\t|O2.5\tU2.5\t|O3.5\tU3.5");
        System.out.println("----------------------------------------------------------");
        for (Result result : resultList) {
            String tmpStr = "";
            if (result.homeScore == 0 && result.awayScore == 1) {
                tmpStr = "|" + oddsInfoMap.get("o1.5") + "\t" + oddsInfoMap.get("u1.5");
            } else if (result.homeScore == 0 && result.awayScore == 2) {
                tmpStr = "\t\t|" + oddsInfoMap.get("o2.5") + "\t" + oddsInfoMap.get("u2.5");
            } else if (result.homeScore == 0 && result.awayScore == 3) {
                tmpStr = "\t\t\t\t|" + oddsInfoMap.get("o3.5") + "\t" + oddsInfoMap.get("u3.5");
            }

            System.out.println(
                    result.homeScore + "-" + result.awayScore + "\t\t|" + result.probability + "\t\t" + tmpStr);

            if (result.homeScore == 0 && result.awayScore == 1) {
                System.out.println("----------------------------------------------------------");
            } else if (result.homeScore == 0 && result.awayScore == 2) {
                System.out.println("----------------------------------------------------------");
            } else if (result.homeScore == 0 && result.awayScore == 3) {
                System.out.println("----------------------------------------------------------");
            }

        }

        System.out.println("\n1: " + oddsInfoMap.get("1"));
        System.out.println("x: " + oddsInfoMap.get("x"));
        System.out.println("2: " + oddsInfoMap.get("2"));
        System.out.println("o2.5: " + oddsInfoMap.get("o2.5"));
        System.out.println("o2.5: " + oddsInfoMap.get("u2.5"));
        System.out.println("o3.5: " + oddsInfoMap.get("o3.5"));
        System.out.println("o3.5: " + oddsInfoMap.get("u3.5"));
    }

    static class Result {
        int homeScore = 0;
        int awayScore = 0;
        float probability = 0;

        Result(int homeScore, int awayScore, float probability) {
            this.homeScore = homeScore;
            this.awayScore = awayScore;
            this.probability = probability;
        }

        @Override
        public String toString() {
            return homeScore + "-" + awayScore + " " + probability;
        }
    }
}
