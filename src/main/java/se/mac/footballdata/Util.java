package se.mac.footballdata;

import se.mac.footballdata.rest.model.Event;
import se.mac.footballdata.rest.model.League;
import se.mac.footballdata.rest.model.Team;
import se.mac.footballdata.sportsapi.SportsApiClient;
import se.mac.footballdata.sportsapi.db.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static final int DECIMALS = 2;

    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Map<Integer, League> leagues = new HashMap<>();

    static {
        leagues.put(1, new League(1, "Premier League", "2026-08-21"));
        leagues.put(12, new League(12, "Championship", "2026-08-14"));
        leagues.put(3, new League(3, "LaLiga", "2026-08-15"));
        leagues.put(4, new League(4, "Serie A", "2026-08-22"));
        leagues.put(5, new League(5, "Bundesliga", "2026-08-28"));
        leagues.put(6, new League(6, "Ligue 1", "2026-08-21"));
        leagues.put(26, new League(26, "Allsvenskan", "2026-04-04"));
        leagues.put(55, new League(55, "Finland", "2026-04-04"));
        leagues.put(54, new League(54, "Norge", "2026-03-14"));
        leagues.put(84, new League(84, "Danmark", "2026-07-24"));
        leagues.put(1000, new League(1000, "Superettan", "2026-04-04"));
        //leagues.put(1001, new League(1001, "Ettan Norra", "2026-04-04"));
        //leagues.put(1002, new League(1002, "Ettan Södra", "2026-04-04"));
    }

    public static Map<Integer, String> arenas = new HashMap<>();

    static {
        arenas.put(427, "Strawberry Arena (Solna) Kapacitet: 50000");
        arenas.put(388, "Nordic Wellness Arena (Gothenburg) Kapacitet: 6300");
        arenas.put(422, "Stora Valla (Degerfors) Kapacitet: 7500");
        arenas.put(416, "3Arena (Stockholm) Kapacitet: 32000");
        arenas.put(421, "Örjans Vall (Halmstad) Kapacitet: 15500");
        arenas.put(417, "Grimsta IP (Stockholm) Kapacitet: 7343");
        arenas.put(419, "Borås Arena (Boras) Kapacitet: 17800");
        arenas.put(418, "Gamla Ullevi (Gothenburg) Kapacitet: 18416");
        arenas.put(423, "PlatinumCars Arena (Norrkoping) Kapacitet: 17234");
        arenas.put(425, "Finnvedsvallen (Varnamo) Kapacitet: 5000");
        arenas.put(426, "Studenternas IP (Uppsala) Kapacitet: 11167");
        arenas.put(711, "Guldfageln Arena (Kalmar) Kapacitet: 12500");
        arenas.put(133, "Eleda Stadium (Malmo) Kapacitet: 22500");
        arenas.put(420, "Strandvallen (Hallevik) Kapacitet: 6500");
        arenas.put(424, "Spiris Arena (Vaxjo) Kapacitet: 12000");
        arenas.put(712, "Hitachi Energy Arena (Vasteras) Kapacitet: 8900");
        // Finland
        arenas.put(398, "Tammela Stadium (Tampere) Kapacitet: 8077");
        arenas.put(357, "Savon Sanomat Areena (Kuopio) Kapacitet: 5300");
        // Norge
        arenas.put(119, "Aspmyra Stadion (Bodø) Kapacitet: 8270");
        arenas.put(391, "Fredrikstad Stadion (Fredrikstad) Kapacitet: 13300");
        arenas.put(139, "Brann Stadion (Bergen) Kapacitet: 17686");
        // Danmark
        arenas.put(830, "Brøndby Stadium (Brondby) Kapacitet: 29000");
        arenas.put(122, "Parken (Copenhagen) Kapacitet: 38190");
        arenas.put(125, "MCH Arena (Herning) Kapacitet: 11809");
        arenas.put(791, "Right to Dream Park (Farum) Kapacitet: 10100");
        arenas.put(824, "Hvidovre Stadion (Copenhagen) Kapacitet: 12000");
        arenas.put(825, "Lyngby Stadium (Lyngby) Kapacitet: 10000");
        arenas.put(828, "Cepheus Park Randers (Randers) Kapacitet: 12000");
        arenas.put(829, "JYSK Park (Silkeborg) Kapacitet: 10000");
        arenas.put(832, "Sydbank Park (Haderslev) Kapacitet: 10000");
        arenas.put(826, "Vejle Stadium (Vejle) Kapacitet: 11060");
        arenas.put(831, "Viborg Stadion (Viborg) Kapacitet: 9566");

        // Premier league
        arenas.put(18, "Emirates Stadium (London) Kapacitet: 60704");
        arenas.put(3, "Villa Park (Birmingham) Kapacitet: 42640");
        arenas.put(2, "Vitality Stadium (Bournemouth) Kapacitet: 11307");
        arenas.put(16, "Brentford Community Stadium (London) Kapacitet: 17250");
        arenas.put(13, "Stamford Bridge (London) Kapacitet: 40341");
        arenas.put(198, "Coventry Building Society Arena (Coventry) Kapacitet: 32609");
        arenas.put(20, "Hill Dickinson Stadium (Liverpool) Kapacitet: 52888");
        arenas.put(199, "MKM Stadium (Hull) Kapacitet: 25586");
        arenas.put(195, "Portman Road Stadium (Ipswich) Kapacitet: 29673");
        arenas.put(19, "Elland Road (Leeds) Kapacitet: 37890");
        arenas.put(1, "Anfield (Liverpool) Kapacitet: 61276");
        arenas.put(12, "Etihad Stadium (Manchester) Kapacitet: 53400");
        arenas.put(17, "Old Trafford (Manchester) Kapacitet: 74879");
        arenas.put(4, "St James' Park (Newcastle upon Tyne) Kapacitet: 52409");
        arenas.put(15, "City Ground (Nottingham) Kapacitet: 30445");
        arenas.put(896, "Boundary Park (Oldham) Kapacitet: 13512");
        arenas.put(7, "Stadium of Light (Sunderland) Kapacitet: 48707");
        arenas.put(890, "County Ground (Swindon) Kapacitet: 15728");
        arenas.put(9, "Tottenham Hotspur Stadium (London) Kapacitet: 62850");
        arenas.put(209, "The Hawthorns (West Bromwich) Kapacitet: 26445");
        arenas.put(8, "London Stadium (London) Kapacitet: 62500");

        // Championship
        arenas.put(864, "Oakwell Ground (Barnsley) Kapacitet: 23287");
        arenas.put(194, "St. Andrew's @ Knighthead Park (Birmingham) Kapacitet: 29409");
        arenas.put(210, "Ewood Park (Blackburn) Kapacitet: 31367");
        arenas.put(714, "Bloomfield Road (Blackpool) Kapacitet: 17338");
        arenas.put(715, "Toughsheet Community Stadium  (Horwich) Kapacitet: 28723");
        arenas.put(866, "Valley Parade (Bradford) Kapacitet: 25136");
        arenas.put(5, "American Express Stadium (Falmer) Kapacitet: 31876");
        arenas.put(212, "Ashton Gate (Bristol) Kapacitet: 27000");
        arenas.put(10, "Turf Moor (Burnley) Kapacitet: 21944");
        arenas.put(870, "Pirelli Stadium (Burton upon Trent) Kapacitet: 6912");
        arenas.put(196, "The Valley (London) Kapacitet: 27111");
        arenas.put(898, "Alexandra Stadium (Crewe) Kapacitet: 10153");
        arenas.put(14, "Selhurst Park (London) Kapacitet: 25456");
        arenas.put(208, "Pride Park Stadium (Derby) Kapacitet: 33597");
        arenas.put(720, "Eco-Power Stadium (Doncaster) Kapacitet: 15231");
        arenas.put(6, "Craven Cottage (London) Kapacitet: 29600");
        arenas.put(883, "Priestfield Stadium (Gillingham) Kapacitet: 11582");
        arenas.put(717, "Accu Stadium (Huddersfield) Kapacitet: 24500");
        arenas.put(213, "King Power Stadium (Leicester) Kapacitet: 32312");
        arenas.put(865, "Sincil Bank (Lincoln) Kapacitet: 10780");
        arenas.put(718, "Kenilworth Road (Luton) Kapacitet: 12056");
        arenas.put(202, "Riverside (Middlesbrough) Kapacitet: 34742");
        arenas.put(205, "The Den (London) Kapacitet: 20146");
        arenas.put(723, "Stadium MK (Milton Keynes) Kapacitet: 30500");
        arenas.put(204, "Carrow Road (Norwich) Kapacitet: 27244");
        arenas.put(206, "Kassam Stadium (Oxford) Kapacitet: 12500");
        arenas.put(721, "Weston Homes Stadium (Peterborough) Kapacitet: 15314");
        arenas.put(725, "Home Park (Plymouth) Kapacitet: 17150");
        arenas.put(716, "Select Car Leasing Stadium (Reading) Kapacitet: 24161");
        arenas.put(875, "New York Stadium (Rotherham) Kapacitet: 12000");
        arenas.put(719, "Glanford Park (Scunthorpe) Kapacitet: 9183");
        arenas.put(211, "Bramall Lane (Sheffield) Kapacitet: 32050");
        arenas.put(214, "Hillsborough Stadium (Sheffield) Kapacitet: 34835");
        arenas.put(200, "St. Mary's Stadium (Southampton) Kapacitet: 32384");
        arenas.put(207, "bet365 Stadium (Stoke) Kapacitet: 30089");
        arenas.put(891, "Bescot Stadium (Walsall) Kapacitet: 11300");
        arenas.put(197, "Vicarage Road (Watford) Kapacitet: 22200");

        arenas.put(79, "Voith-Arena (Heidenheim an der Brenz) Kapacitet: 15000");
        arenas.put(753, "Fritz-Walter-Stadion (Kaiserslautern) Kapacitet: 49780");
        arenas.put(92, "RheinEnergieStadion (Cologne) Kapacitet: 50000");
        arenas.put(752, "Max-Morlock-Stadion (Nuremberg) Kapacitet: 50000");
        arenas.put(81, "An der alten Försterei (Berlin) Kapacitet: 22012");
        arenas.put(91, "MEWA Arena (Mainz) Kapacitet: 34000");
        arenas.put(83, "BayArena (Leverkusen) Kapacitet: 30210");
        arenas.put(90, "Signal Iduna Park (Dortmund) Kapacitet: 81365");
        arenas.put(93, "Borussia-Park (Monchengladbach) Kapacitet: 54057");
        arenas.put(760, "Merck-Stadion am Böllenfalltor (Darmstadt) Kapacitet: 16500");
        arenas.put(85, "Deutsche Bank Park (Frankfurt) Kapacitet: 58000");
        arenas.put(88, "WWK ARENA (Augsburg) Kapacitet: 30660");
        arenas.put(77, "Allianz Arena (Munich) Kapacitet: 75024");
        arenas.put(759, "Audi Sportpark (Ingolstadt) Kapacitet: 15200");
        arenas.put(755, "VELTINS-Arena (Gelsenkirchen) Kapacitet: 62271");
        arenas.put(89, "Millerntor-Stadion (Hamburg - St. Pauli) Kapacitet: 29546");
        arenas.put(756, "Merkur Spiel-Arena (Dusseldorf) Kapacitet: 54600");
        arenas.put(94, "Volksparkstadion (Hamburg) Kapacitet: 57000");
        arenas.put(754, "Heinz-von-Heiden-Arena (Hannover) Kapacitet: 49200");
        arenas.put(761, "Holstein-Stadion (Kiel) Kapacitet: 15034");
        arenas.put(78, "Red Bull Arena (Leipzig) Kapacitet: 47069");
        arenas.put(87, "Europa-Park-Stadion (Freiburg im Breisgau) Kapacitet: 34700");
        arenas.put(758, "Home Deluxe Arena (Paderborn) Kapacitet: 15000");
        arenas.put(757, "Sportpark Ronhof Thomas Sommer (Fürth) Kapacitet: 15000");
        arenas.put(86, "Weserstadion (Bremen) Kapacitet: 42100");

    }

    public static Integer getLeagueIdByName(String name) throws Exception {
        for (Map.Entry<Integer, League> entry : leagues.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(name)) {
                return entry.getKey();
            }
        }
        throw new Exception("Ingen liga med namn " + name + " hittades");
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
        StringBuilder formStr = new StringBuilder();

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

                if (formStr.length() < 5) {
                    if (match.fullTimeHomeGoals > match.fullTimeAwayGoals) {
                        formStr.append("V");
                    } else if (match.fullTimeHomeGoals < match.fullTimeAwayGoals) {
                        formStr.append("F");
                    } else {
                        formStr.append("O");
                    }
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

                if (formStr.length() < 5) {
                    if (match.fullTimeAwayGoals > match.fullTimeHomeGoals) {
                        formStr.append("V");
                    } else if (match.fullTimeAwayGoals < match.fullTimeHomeGoals) {
                        formStr.append("F");
                    } else {
                        formStr.append("O");
                    }
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
        t.form = formStr.toString();
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
        eventDB.time = convertUtcTimeToLocal(String.valueOf(event.eventDate));
        eventDB.round = event.roundNumber;
        eventDB.hometeam = event.homeTeam;
        eventDB.awayteam = event.awayTeam;
        eventDB.homeScore = event.homeScore;
        eventDB.awayScore = event.awayScore;
        eventDB.homeScoreHt = event.homeScoreHt;
        eventDB.awayScoreHt = event.awayScoreHt;
        if (event.refereeId != null) {
            eventDB.refereeId = event.refereeId;
        }
        eventDB.weather = formatWeather(event);
        eventDB.venue = Util.arenas.get(event.venueId);
        return eventDB;
    }

    public static String formatWeather(SportsApiClient.Event event) {
        if (event == null || event.weather == null) {
            return "";
        }

        String desc = event.weather.description;
        if (desc == null || desc.isBlank()) {
            return "";
        }

        return String.format(
                "Weather: %s, %.1f°C, wind %.1f km/h%n",
                desc,
                event.weather.temperatureC,
                event.weather.windSpeed
        );
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

    public static LineupDB createLineupDB(String lineup_status, SportsApiClient.Lineups lineups, int eventId) {
        LineupDB lineupDB = new LineupDB();
        lineupDB.status = lineup_status;
        lineupDB.eventId = eventId;
        lineupDB.homeTeam = lineups.home.team_name;
        lineupDB.awayTeam = lineups.away.team_name;
        lineupDB.homeTeamId = lineups.home.team_id;
        lineupDB.awayTeamId = lineups.away.team_id;
        lineupDB.homeFormation = lineups.home.formation;
        lineupDB.awayFormation = lineups.away.formation;
        for (SportsApiClient.Player p : lineups.home.players) {
            lineupDB.homePlayers.add(createLineupPlayerDB(p));
        }
        for (SportsApiClient.Player p : lineups.away.players) {
            lineupDB.awayPlayers.add(createLineupPlayerDB(p));
        }
        for (SportsApiClient.Player p : lineups.home.substitutes) {
            lineupDB.homeSubstitutes.add(createLineupPlayerDB(p));
        }
        for (SportsApiClient.Player p : lineups.away.substitutes) {
            lineupDB.awaySubstitutes.add(createLineupPlayerDB(p));
        }
        return lineupDB;
    }

    public static LineupPlayerDB createLineupPlayerDB(SportsApiClient.Player p) {
        LineupPlayerDB playerDB = new LineupPlayerDB();
        playerDB.playerId = p.id;
        playerDB.name = p.name;
        playerDB.position = p.position;
        playerDB.jerseyNumber = p.jerseyNumber != null ? p.jerseyNumber : 0;
        return playerDB;
    }

    public static PlayerDB createPlayerDB(SportsApiClient.Player p, int leagueId) {
        PlayerDB playerDB = new PlayerDB();
        playerDB.leagueId = leagueId;
        playerDB.teamId = p.currentTeamId;
        playerDB.playerId = p.id;
        playerDB.name = p.name;
        playerDB.position = p.position;
        playerDB.jerseyNumber = p.jerseyNumber != null ? p.jerseyNumber : 0;
        playerDB.rating = p.rating != null ? p.rating : 0;
        playerDB.nationality = p.nationality;
        return playerDB;
    }

    public static OddsDB createOddsDB(List<SportsApiClient.OddsLine> results, int eventId) {
        OddsDB oddsDB = new OddsDB();
        oddsDB.eventId = eventId;

        for (SportsApiClient.OddsLine ol : results) {
            switch (ol.market) {
                case "1x2" -> {
                    switch (ol.outcome) {
                        case "HOME" -> oddsDB.homeWin = ol.decimalOdds;
                        case "DRAW" -> oddsDB.draw = ol.decimalOdds;
                        case "AWAY" -> oddsDB.awayWin = ol.decimalOdds;
                    }
                }
                case "over_under_15" -> {
                    if (ol.outcome.startsWith("over")) {
                        oddsDB.over15Goals = ol.decimalOdds;
                    } else {
                        oddsDB.under15Goals = ol.decimalOdds;
                    }
                }
                case "over_under_25" -> {
                    if (ol.outcome.startsWith("over")) {
                        oddsDB.over25Goals = ol.decimalOdds;
                    } else {
                        oddsDB.under25Goals = ol.decimalOdds;
                    }
                }
                case "over_under_35" -> {
                    if (ol.outcome.startsWith("over")) {
                        oddsDB.over35Goals = ol.decimalOdds;
                    } else {
                        oddsDB.under35Goals = ol.decimalOdds;
                    }
                }
                case "btts" -> {
                    if (ol.outcome.startsWith("yes")) {
                        oddsDB.bttsYes = ol.decimalOdds;
                    } else {
                        oddsDB.bttsNo = ol.decimalOdds;
                    }
                }
                case "total_corners" -> {
                    if (ol.outcomeName.equals("Over 9.50")) {
                        oddsDB.over95Corners = ol.decimalOdds;
                    } else if (ol.outcomeName.equals("Under 9.50")) {
                        oddsDB.under95Corners = ol.decimalOdds;
                    } else if (ol.outcomeName.equals("Over 10.50")) {
                        oddsDB.over105Corners = ol.decimalOdds;
                    } else if (ol.outcomeName.equals("Under 10.50")) {
                        oddsDB.under105Corners = ol.decimalOdds;
                    }
                }
            }
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
        fixtureDB.time = convertUtcTimeToLocal(String.valueOf(event.eventDate));
        fixtureDB.hometeam = event.homeTeam;
        fixtureDB.awayteam = event.awayTeam;
        if (event.refereeId != null) {
            fixtureDB.refereeId = event.refereeId;
        }
        if (event.headToHead != null) {
            fixtureDB.headToHead = createHeadToHeadDB(event.headToHead);
        }
        fixtureDB.venue = Util.arenas.get(event.venueId);
        return fixtureDB;
    }

    public static String convertUtcTimeToLocal(String utcTimestamp) {
        // Parse the timestamp as an OffsetDateTime (accepts missing seconds)
        OffsetDateTime odt = OffsetDateTime.parse(utcTimestamp);

        // Convert to your timezone
        ZoneId localZone = ZoneId.of("Europe/Stockholm");
        ZonedDateTime local = odt.atZoneSameInstant(localZone);

        // Return only HH:mm
        return local.toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }

    private static HeadToHeadDB createHeadToHeadDB(SportsApiClient.HeadToHead headToHead) {
        HeadToHeadDB db = new HeadToHeadDB();
        db.totalMatches = headToHead.totalMatches;
        db.homeWins = headToHead.homeWins;
        db.draws = headToHead.draws;
        db.awayWins = headToHead.awayWins;
        db.homeGoals = headToHead.homeGoals;
        db.awayGoals = headToHead.awayGoals;
        db.avgTotalGoals = Util.round(headToHead.avgTotalGoals, 2);
        db.homeWinRate = Util.round(headToHead.homeWinRate, 2);
        db.awayWinRate = Util.round(headToHead.awayWinRate, 2);
        if (headToHead.recentMatches != null) {
            db.recentMatches = createRecentMatches(headToHead.recentMatches);
        }
        return db;
    }

    private static List<RecentMatchDB> createRecentMatches(List<SportsApiClient.RecentMatch> recentMatches) {
        ArrayList<RecentMatchDB> matchesList = new ArrayList<>();
        for (SportsApiClient.RecentMatch m : recentMatches) {
            RecentMatchDB db = new RecentMatchDB();
            db.date = m.date.toString().substring(0, 10);
            db.home = m.home;
            db.away = m.away;
            db.score = m.score;
            matchesList.add(db);
        }
        return matchesList;
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
