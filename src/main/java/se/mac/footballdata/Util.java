package se.mac.footballdata;

import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.League;
import se.mac.footballdata.rest.model.Team;
import se.mac.footballdata.sportsapi.SportsApiClient;
import se.mac.footballdata.sportsapi.db.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            55, new League(55, "Finland"),
            1000, new League(1000, "Superettan")
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

    // Util
    public static void updateRedcards(EventDB db, List<IncidentDB> incidentList) {
        int redcardsHome = 0;
        int redcardsAway = 0;
        for (IncidentDB incidentDB : incidentList) {
            if (incidentDB.cardType != null) {
                if (incidentDB.cardType.equalsIgnoreCase("red")) {
                    if (incidentDB.home) {
                        redcardsHome++;
                    } else {
                        redcardsAway++;
                    }
                }
            }
        }
        db.hr = redcardsHome;
        db.ar = redcardsAway;
    }

    public static List<EventDB> createEventDB(List<SportsApiClient.Event> eventList) {
        return eventList.stream()
                .map(Util::createEventDB)
                .toList();
    }

    public static EventDB createEventDB(SportsApiClient.Event event) {
        EventDB eventDB = new EventDB();
        eventDB.eventId = event.id;
        eventDB.leagueId = event.leagueId;
        eventDB.seasonId = event.seasonId;
        eventDB.date = event.eventDate.toString().substring(0, 10);
        eventDB.time = event.eventDate.toString().substring(11, 16);
        eventDB.round = event.roundNumber;
        eventDB.hometeam = event.homeTeam;
        eventDB.awayteam = event.awayTeam;
        eventDB.homeScore = event.homeScore;
        eventDB.awayScore = event.awayScore;
        eventDB.homeScoreHt = event.homeScoreHt;
        eventDB.awayScoreHt = event.awayScoreHt;
        if (event.refereeId != null)
            eventDB.refereeId = event.refereeId;
        return eventDB;
    }

    public static List<IncidentDB> createIncidentDB(List<SportsApiClient.Incident> incidents, int eventId) {
        ArrayList<IncidentDB> incidentDBList = new ArrayList<>();

        for (SportsApiClient.Incident i : incidents) {
            IncidentDB incidentDB = new IncidentDB();
            incidentDB.eventId = eventId;
            incidentDB.type = i.type;
            incidentDB.text = i.text;
            incidentDB.home = i.is_home != null ? i.is_home : false;
            incidentDB.player = i.player;
            incidentDB.playerId = i.player_id != null ? i.player_id : 0;
            incidentDB.cardType = i.card_type;
            incidentDB.goalType = i.goal_type;
            incidentDB.assist = i.assist;
            incidentDB.minute = i.minute;
            incidentDB.playerIn = i.player_in;
            incidentDB.playerOut = i.player_out;
            incidentDBList.add(incidentDB);
        }
        return incidentDBList;
    }

    public static LineupDB createLineupDB(SportsApiClient.Lineups lineups, int eventId) {
        LineupDB lineupDB = new LineupDB();
        lineupDB.eventId = eventId;
        lineupDB.homeTeam = lineups.home.team_name;
        lineupDB.awayTeam = lineups.away.team_name;
        lineupDB.homeTeamId = lineups.home.team_id;
        lineupDB.awayTeamId = lineups.away.team_id;
        lineupDB.homeFormation = lineups.home.formation;
        lineupDB.awayFormation = lineups.away.formation;
        for (SportsApiClient.Player p : lineups.home.players) {
            lineupDB.homePlayers.add(createPlayerDB(p));
        }
        for (SportsApiClient.Player p : lineups.away.players) {
            lineupDB.awayPlayers.add(createPlayerDB(p));
        }
        for (SportsApiClient.Player p : lineups.home.substitutes) {
            lineupDB.homeSubstitutes.add(createPlayerDB(p));
        }
        for (SportsApiClient.Player p : lineups.away.substitutes) {
            lineupDB.awaySubstitutes.add(createPlayerDB(p));
        }
        return lineupDB;
    }

    private static PlayerDB createPlayerDB(SportsApiClient.Player p) {
        PlayerDB playerDB = new PlayerDB();
        playerDB.playerId = p.id;
        playerDB.name = p.name;
        playerDB.position = p.position;
        playerDB.jerseyNumber = p.jerseyNumber;
        return playerDB;
    }
    public static OddsDB createOddsDB(List<SportsApiClient.OddsLine> results, int eventId) {
        OddsDB oddsDB = new OddsDB();
        oddsDB.eventId = eventId;

        for (SportsApiClient.OddsLine ol : results) {
            if (ol.market.equals("1x2")) {
                if (ol.outcome.equals("HOME")) {
                    oddsDB.homeWin = ol.decimalOdds;
                } else if (ol.outcome.equals("DRAW")) {
                    oddsDB.draw = ol.decimalOdds;
                } else if (ol.outcome.equals("AWAY")) {
                    oddsDB.awayWin = ol.decimalOdds;
                }
            } else if (ol.market.equals("over_under_25")) {
                if (ol.outcome.startsWith("over")) {
                    oddsDB.over25Goals = ol.decimalOdds;
                } else {
                    oddsDB.under25Goals = ol.decimalOdds;
                }
            } else if (ol.market.equals("btts")) {
                if (ol.outcome.startsWith("yes")) {
                    oddsDB.bttsYes = ol.decimalOdds;
                } else {
                    oddsDB.bttsNo = ol.decimalOdds;
                }
            }
            System.out.println("  " + ol);
        }
        return oddsDB;
    }

    public static List<FixtureDB> createFixtureDB(List<SportsApiClient.Event> eventList) {
        return eventList.stream()
                .map(Util::createFixtureDB)
                .toList();
    }

    public static FixtureDB createFixtureDB(SportsApiClient.Event event) {
        FixtureDB fixtureDB = new FixtureDB();
        fixtureDB.eventId = event.id;
        fixtureDB.leagueId = event.leagueId;
        fixtureDB.seasonId = event.seasonId;
        fixtureDB.date = event.eventDate.toString().substring(0, 10);
        fixtureDB.time = event.eventDate.toString().substring(11, 16);
        fixtureDB.hometeam = event.homeTeam;
        fixtureDB.awayteam = event.awayTeam;
        if (event.refereeId != null)
            fixtureDB.refereeId = event.refereeId;
        return fixtureDB;
    }

    public static List<RefereeDB> createRefereeDB(List<SportsApiClient.Referee> refereeList, int leagueId) {
        return refereeList.stream()
                .map(referee -> createRefereeDB(referee, leagueId))
                .toList();
    }

    public static RefereeDB createRefereeDB(SportsApiClient.Referee referee, int leagueId) {
        RefereeDB refereeDB = new RefereeDB();
        refereeDB.refereeId = referee.id;
        refereeDB.name = referee.name;
        refereeDB.leagueId = leagueId;
        refereeDB.matches = referee.matches;
        refereeDB.totalYellowCards = referee.totalYellowCards;
        refereeDB.totalRedCards = referee.totalRedCards;
        refereeDB.avgYellowPerMatch = referee.avgYellowPerMatch;
        refereeDB.avgRedPerMatch = referee.avgRedPerMatch;
        return refereeDB;
    }
}
