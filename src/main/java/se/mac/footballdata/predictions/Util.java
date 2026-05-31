package se.mac.footballdata.predictions;

import se.mac.footballdata.predictions.model.Match;
import se.mac.footballdata.predictions.model.Team;

import java.math.BigDecimal;
import java.util.List;

public class Util {

    public static int getTotalHomeScored(List<Match> matchList) {
        int no = 0;
        for (Match m : matchList) {
            no += m.getHomeScore();
        }
        return no;
    }

    public static int getTotalAwayScored(List<Match> matchList) {
        int no = 0;
        for (Match m : matchList) {
            no += m.getAwayScore();
        }
        return no;
    }

    public static int getTotalHomeConceded(List<Match> matchList) {
        return getTotalAwayScored(matchList);
    }

    public static int getTotalAwayConceded(List<Match> matchList) {
        return getTotalHomeScored(matchList);
    }

    public static AvgGoalInfo getAvgGoalInfo(Team homeTeam, Team awayTeam) {

        AvgGoalInfo avgGoalInfo = new AvgGoalInfo();
        avgGoalInfo.homegoals_team = calculateMean(homeTeam.getNumberOfHomeMatches(), homeTeam.getHomeGoalsScored());
        avgGoalInfo.awaygoals_team = calculateMean(awayTeam.getNumberOfAwayMatches(), awayTeam.getAwayGoalsScored());
        avgGoalInfo.homeconceded_team = calculateMean(homeTeam.getNumberOfHomeMatches(),
                homeTeam.getHomeGoalsConceded());
        avgGoalInfo.awayconceded_team = calculateMean(awayTeam.getNumberOfAwayMatches(),
                awayTeam.getAwayGoalsConceded());

        int totalMatches = homeTeam.getNumberOfHomeMatches() + homeTeam.getNumberOfAwayMatches();
        avgGoalInfo.homeTeamGoals = calculateMean(totalMatches,
                homeTeam.getHomeGoalsScored() + homeTeam.getAwayGoalsScored());
        avgGoalInfo.homeTeamConceded = calculateMean(totalMatches,
                homeTeam.getHomeGoalsConceded() + homeTeam.getAwayGoalsConceded());

        totalMatches = awayTeam.getNumberOfHomeMatches() + awayTeam.getNumberOfAwayMatches();
        avgGoalInfo.awayTeamGoals = calculateMean(totalMatches,
                awayTeam.getHomeGoalsScored() + awayTeam.getAwayGoalsScored());
        avgGoalInfo.awayTeamConceded = calculateMean(totalMatches,
                awayTeam.getHomeGoalsConceded() + awayTeam.getAwayGoalsConceded());

        return avgGoalInfo;
    }

    public static AvgCornerInfo getAvgCornerInfo(Team homeTeam, Team awayTeam) {
        AvgCornerInfo avgCornerInfo = new AvgCornerInfo();
        avgCornerInfo.homeCorner_team = calculateMean(homeTeam.getNumberOfHomeMatches() + homeTeam.getNumberOfAwayMatches(), homeTeam.getTotalCorners());
        avgCornerInfo.awayCorner_team = calculateMean(awayTeam.getNumberOfAwayMatches() + awayTeam.getNumberOfHomeMatches(), awayTeam.getTotalCorners());

        return avgCornerInfo;
    }

    public static double calculateMean(int matches, int goals) {
        double noOfMatches = matches;
        double noOfGoals = goals;
        double d = noOfGoals / noOfMatches;
        return d;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}
