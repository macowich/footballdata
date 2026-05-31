package se.mac.footballdata;

import se.mac.footballdata.rest.Result;
import se.mac.footballdata.rest.Team;

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

    public static Team createTeamFromResult(String name, List<Result> matches, String filter) {
        Team t = new Team();
        t.name = name;

        int homeGoalsTotal = 0;
        int awayGoalsTotal = 0;
        int homeMatchesTotal = 0;
        int awayMatchesTotal = 0;
        int homeGoals = 0;
        int homeGoalsConceeded = 0;
        int awayGoals = 0;
        int awayGoalsConceeded = 0;
        int homeMatches = 0;
        int awayMatches = 0;
        int over2Counter = 0;

        for (Result match : matches) {

            if (filter != null && filter.equalsIgnoreCase("home")) {
                homeGoalsTotal += match.fullTimeHomeGoals;

                if (name.equals(match.homeTeam)) {
                    homeGoals += match.fullTimeHomeGoals;
                    homeGoalsConceeded += match.fullTimeAwayGoals;
                    homeMatches++;
                    if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                        over2Counter++;
                    }
                }
            } else if (filter != null && filter.equalsIgnoreCase("away")) {
                awayGoalsTotal += match.fullTimeAwayGoals;

                if (name.equals(match.awayTeam)) {
                    awayGoals += match.fullTimeAwayGoals;
                    awayGoalsConceeded += match.fullTimeHomeGoals;
                    awayMatches++;
                    if (match.fullTimeHomeGoals + match.fullTimeAwayGoals > 2) {
                        over2Counter++;
                    }
                }
            } else {
                homeGoalsTotal += match.fullTimeHomeGoals;
                awayGoalsTotal += match.fullTimeAwayGoals;
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
        }

        int totalGoals = homeGoals + awayGoals + homeGoalsConceeded + awayGoalsConceeded;

        double avgHomeGoalsTotal =
                matches.isEmpty()
                        ? 0
                        : (double) homeGoalsTotal / (homeMatchesTotal/2);

        double avgAwayGoalsTotal =
                matches.isEmpty()
                        ? 0
                        : (double) awayGoalsTotal / (awayMatchesTotal/2);

        double avgGoals =
                matches.isEmpty()
                        ? 0
                        : (double) totalGoals / (homeMatches + awayMatches);

        double avgHomeGoals =
                homeMatches == 0
                        ? 0
                        : (double) (homeGoals + homeGoalsConceeded) / homeMatches;

        double avgAwayGoals =
                awayMatches == 0
                        ? 0
                        : (double) (awayGoals + awayGoalsConceeded) / awayMatches;

        double avgHomeGoalsConceeded =
                homeMatches == 0
                        ? 0
                        : (double) (homeGoalsConceeded) / homeMatches;

        double avgAwayGoalsConceeded =
                awayMatches == 0
                        ? 0
                        : (double) (awayGoalsConceeded) / awayMatches;

        t.matcher = homeMatches + awayMatches;
        t.goals = homeGoals + awayGoals;
        t.avgHomeGoalsTotal = Util.round(avgHomeGoalsTotal, 2);
        t.avgAwayGoalsTotal = Util.round(avgAwayGoalsTotal, 2);
        t.goalsconceeded = homeGoalsConceeded + awayGoalsConceeded;
        t.avgGoals = Util.round(avgGoals, 2);
        t.avgHomeGoals = Util.round(avgHomeGoals, 2);
        t.avgHomeGoalsConceeded = Util.round(avgHomeGoalsConceeded, 2);
        t.avgAwayGoalsConceeded = Util.round(avgAwayGoalsConceeded, 2);
        t.avgAwayGoals = Util.round(avgAwayGoals, 2);
        t.over2 = over2Counter;
        return t;
    }
}
