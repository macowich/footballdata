package se.mac.footballdata;

import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.Team;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Util {
    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

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
        int awayGoalsTotal = 0;
        int homeConcededTotal = 0;
        int awayConcededTotal = 0;
        int homeMatchesTotal = 0;
        int awayMatchesTotal = 0;

        int homeGoals = 0;
        int homeGoalsConceeded = 0;
        int awayGoals = 0;
        int awayGoalsConceeded = 0;

        int homeMatches = 0;
        int awayMatches = 0;

        int over2Counter = 0;

        for (Event match : matches) {
            //TODO
            homeGoalsTotal += match.fullTimeHomeGoals;
            awayGoalsTotal += match.fullTimeAwayGoals;
            homeConcededTotal += match.fullTimeAwayGoals;
            awayConcededTotal += match.fullTimeHomeGoals;
            homeMatchesTotal++;
            awayMatchesTotal++;

            if (name.equals(match.homeTeam)) {
                homeGoals += match.fullTimeHomeGoals;
                homeGoalsConceeded += match.fullTimeAwayGoals;
                homeMatches++;
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                    over2Counter++;
                }
            } else if (name.equals(match.awayTeam)) {
                awayGoals += match.fullTimeAwayGoals;
                awayGoalsConceeded += match.fullTimeHomeGoals;
                awayMatches++;
                if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                    over2Counter++;
                }
            }
        }

        int totalGoals = homeGoals + awayGoals + homeGoalsConceeded + awayGoalsConceeded;

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
        t.avgGoals = Util.round(avgGoals, 2);

        t.homeMatches = homeMatches;
        t.awayMatches = awayMatches;

        t.goalshome = homeGoals;
        t.goalsaway = awayGoals;
        t.conceededhome = homeGoalsConceeded;
        t.conceededaway = awayGoalsConceeded;
        t.avgGoalsHome = Util.round(avgGoalsHome, 2);
        t.avgGoalsAway = Util.round(avgGoalsAway, 2);

        t.goalsHomeTeam =
                homeMatches == 0
                        ? 0
                        : (double) homeGoals / homeMatches;
        t.goalsHomeTeam = Util.round(t.goalsHomeTeam, 2);

        t.concededHomeTeam =
                homeMatches == 0
                        ? 0
                        : (double) homeGoalsConceeded / homeMatches;
        t.concededHomeTeam = Util.round(t.concededHomeTeam, 2);

        t.goalsAwayTeam =
                awayMatches == 0
                        ? 0
                        : (double) awayGoals / awayMatches;
        t.goalsAwayTeam = Util.round(t.goalsAwayTeam, 2);

        t.concededAwayTeam =
                awayMatches == 0
                        ? 0
                        : (double) awayGoalsConceeded / awayMatches;
        t.concededAwayTeam = Util.round(t.concededAwayTeam, 2);

        t.goalsHomeLeague =
                homeMatchesTotal == 0
                        ? 0
                        : (double) homeGoalsTotal / homeMatchesTotal;
        t.goalsHomeLeague = Util.round(t.goalsHomeLeague, 2);

        t.concededHomeLeague =
                homeMatchesTotal == 0
                        ? 0
                        : (double) homeConcededTotal / homeMatchesTotal;
        t.concededHomeLeague = Util.round(t.concededHomeLeague, 2);

        // TODO
        t.over2 = over2Counter;
        return t;
    }
}
