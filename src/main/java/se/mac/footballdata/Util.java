package se.mac.footballdata;

import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.League;
import se.mac.footballdata.rest.model.Team;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Util {

    private static final int DECIMALS = 2;

    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Map<Integer, League> leagues = Map.of(
            1, new League(1, "Premier League"),
            26, new League(26, "Allsvenskan"),
            55, new League(55, "Finland")
    );

    public static String getLeagueName(String code) {
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

    public static String toIso(String dateStr) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter isoFormat = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate date = LocalDate.parse(dateStr, inputFormat);
        return date.format(isoFormat);
    }

    public static Team createTeamFromEvent(String name, List<Event> matches) {
        Team t = new Team();
        t.name = name;

        int homeGoalsTotal = 0;
        int homeConcededTotal = 0;
        int homeMatchesTotal = 0;

        int homeGoals = 0;
        int homeGoalsConceeded = 0;
        int awayGoals = 0;
        int awayGoalsConceeded = 0;
        int homeCorners = 0;
        int awayCorners = 0;
        int homeCornersConceeded = 0;
        int awayCornersConceeded = 0;

        int homeMatches = 0;
        int awayMatches = 0;

        int over2Counter = 0;
        int over1Counter = 0;
        int bttsCounter = 0;
        int cleanSheetsCounter = 0;

        for (Event match : matches) {
            //TODO
            homeGoalsTotal += match.fullTimeHomeGoals;
            homeConcededTotal += match.fullTimeAwayGoals;
            homeMatchesTotal++;

            if (name.equals(match.homeTeam)) {
                homeGoals += match.fullTimeHomeGoals;
                homeGoalsConceeded += match.fullTimeAwayGoals;
                homeCorners += match.homeCorners;
                homeCornersConceeded += match.awayCorners;
                homeMatches++;
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 1) {
                    over1Counter++;
                }
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                    over2Counter++;
                }
                if (match.fullTimeHomeGoals > 0 && match.fullTimeAwayGoals > 0) {
                    bttsCounter++;
                }
                if (match.fullTimeAwayGoals == 0) {
                    cleanSheetsCounter++;
                }
            } else if (name.equals(match.awayTeam)) {
                awayGoals += match.fullTimeAwayGoals;
                awayGoalsConceeded += match.fullTimeHomeGoals;
                awayCorners += match.awayCorners;
                awayCornersConceeded += match.homeCorners;
                awayMatches++;
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 1) {
                    over1Counter++;
                }
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                    over2Counter++;
                }
                if (match.fullTimeHomeGoals > 0 && match.fullTimeAwayGoals > 0) {
                    bttsCounter++;
                }
                if (match.fullTimeHomeGoals == 0) {
                    cleanSheetsCounter++;
                }
            }
        }

        int totalGoals = homeGoals + awayGoals + homeGoalsConceeded + awayGoalsConceeded;
        int totalCorners = homeCorners + awayCorners + homeCornersConceeded + awayCornersConceeded;

        double avgGoals =
                matches.isEmpty()
                        ? 0
                        : (double) totalGoals / (homeMatches + awayMatches);

        double avgGoalsHome =
                homeMatches == 0
                        ? 0
                        : (double) (homeGoals + homeGoalsConceeded) / homeMatches;

        double avgGoalsAway =
                awayMatches == 0
                        ? 0
                        : (double) (awayGoals + awayGoalsConceeded) / awayMatches;

        t.matches = homeMatches + awayMatches;
        t.goals = homeGoals + awayGoals;
        t.conceeded = homeGoalsConceeded + awayGoalsConceeded;
        t.avgGoals = Util.round(avgGoals, DECIMALS);

        t.homeMatches = homeMatches;
        t.awayMatches = awayMatches;

        t.goalshome = homeGoals;
        t.goalsaway = awayGoals;
        t.conceededhome = homeGoalsConceeded;
        t.conceededaway = awayGoalsConceeded;
        t.avgGoalsHome = Util.round(avgGoalsHome, DECIMALS);
        t.avgGoalsAway = Util.round(avgGoalsAway, DECIMALS);

        t.goalsHomeTeam =
                homeMatches == 0
                        ? 0
                        : (double) homeGoals / homeMatches;
        t.goalsHomeTeam = Util.round(t.goalsHomeTeam, DECIMALS);

        t.concededHomeTeam =
                homeMatches == 0
                        ? 0
                        : (double) homeGoalsConceeded / homeMatches;
        t.concededHomeTeam = Util.round(t.concededHomeTeam, DECIMALS);

        t.goalsAwayTeam =
                awayMatches == 0
                        ? 0
                        : (double) awayGoals / awayMatches;
        t.goalsAwayTeam = Util.round(t.goalsAwayTeam, DECIMALS);

        t.concededAwayTeam =
                awayMatches == 0
                        ? 0
                        : (double) awayGoalsConceeded / awayMatches;
        t.concededAwayTeam = Util.round(t.concededAwayTeam, DECIMALS);

        t.goalsHomeLeague =
                homeMatchesTotal == 0
                        ? 0
                        : (double) homeGoalsTotal / homeMatchesTotal;
        t.goalsHomeLeague = Util.round(t.goalsHomeLeague, DECIMALS);

        t.concededHomeLeague =
                homeMatchesTotal == 0
                        ? 0
                        : (double) homeConcededTotal / homeMatchesTotal;
        t.concededHomeLeague = Util.round(t.concededHomeLeague, DECIMALS);

        double avgCorners =
                matches.isEmpty()
                        ? 0
                        : (double) totalCorners / (homeMatches + awayMatches);
        t.avgCorners = Util.round(avgCorners, DECIMALS);

        double avgCornersHome =
                homeMatches == 0
                        ? 0
                        : (double) (homeCorners + homeCornersConceeded) / homeMatches;
        t.avgCornersHome = Util.round(avgCornersHome, DECIMALS);

        double avgCornersAway =
                awayMatches == 0
                        ? 0
                        : (double) (awayCorners + awayCornersConceeded) / awayMatches;
        t.avgCornersAway = Util.round(avgCornersAway, DECIMALS);
        t.over1 = over1Counter;
        t.over2 = over2Counter;
        t.btts = bttsCounter;
        t.cleanSheets = cleanSheetsCounter;
        return t;
    }
}
