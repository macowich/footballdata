package se.mac.footballdata.sportsapi.db;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import se.mac.footballdata.DBUtil;
import se.mac.footballdata.Util;
import se.mac.footballdata.sportsapi.SportsApiClient;
import se.mac.footballdata.sportsapi.stats.EventStats;
import se.mac.footballdata.sportsapi.stats.EventStatsClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static se.mac.footballdata.DBUtil.getLatestEventDate;
import static se.mac.footballdata.DBUtil.updatePlayersCollection;
import static se.mac.footballdata.Util.*;

public class SportsApiLoader {
    private final static String DB_CONNECTION_STRING = "mongodb://localhost:27017";

    private static final SportsApiClient sportsApiClient = new SportsApiClient();
    private static final EventStatsClient eventStatsClient = new EventStatsClient();

    public static void main(String[] args) {
        CodecProvider pojoCodecProvider =
                PojoCodecProvider.builder().automatic(true).build();

        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(pojoCodecProvider)
                );

        try (MongoClient mongoClient = MongoClients.create(DB_CONNECTION_STRING)) {

            MongoDatabase database = mongoClient
                    .getDatabase("sportsdb")
                    .withCodecRegistry(pojoCodecRegistry);

            handleFixturesData(database, 26);
            handleFixturesData(database, 55);
            handleLeague(database, 26);
            handleLeague(database, 55);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void handleLeague(MongoDatabase database, int leagueId) throws Exception {
        handleEventsData(database, leagueId);
        //handlePlayersData(database, leagueId);
        //handleRefereeData(database, leagueId);
    }

    private static void handlePlayersData(MongoDatabase database, int leagueId) throws Exception {
        SportsApiClient.TeamsResponse response = sportsApiClient.fetchTeams(leagueId);
        System.out.printf("Total teams: %d%n%n", response.count);

        ArrayList<PlayerDB> playerDBList = new ArrayList<>();
        for (SportsApiClient.Team team : response.results) {
            SportsApiClient.PlayersResponse playersResponse = sportsApiClient.fetchPlayers(team.id);
            for (SportsApiClient.Player p : playersResponse.results) {
                PlayerDB playerDB = createPlayerDB(p, leagueId);
                playerDB.teamName = team.name; // ToDo remove
                playerDBList.add(playerDB);
            }
        }

        updatePlayersCollection(database, playerDBList);
    }

    static void handleFixturesData(MongoDatabase database, int leagueId) throws Exception {
        String currentDate = LocalDate.now().toString();
        String toDate = String.valueOf(LocalDate.now().plusDays(7));
        System.out.println("Loading fixtures for league" + leagueId + " from " + currentDate + " to " + toDate);
        SportsApiClient.EventsResponse response = sportsApiClient.
                fetchEvents(currentDate, toDate, leagueId, "notstarted");

        List<SportsApiClient.Event> eventList = new ArrayList<>(response.results);
        ArrayList<LineupDB> lineupDBList = new ArrayList<>();
        List<FixtureDB> fixtureDBList = createFixtureDB(eventList);
        for (FixtureDB db : fixtureDBList) {
            loadFixtureOdds(db);
            loadPredictions(db);
            LineupDB lineupDB = loadEventLineups(db.eventId);
            if (lineupDB != null) {
                lineupDBList.add(lineupDB);
            }
        }

        DBUtil.updateFixturesCollection(database, fixtureDBList);
        DBUtil.updateLineupsCollection(database, lineupDBList);
    }

    static void handleEventsData(MongoDatabase database, int leagueId) throws Exception {

        List<SportsApiClient.Event> eventList = new ArrayList<>();
        LocalDate startDate = getStartDate(leagueId, database);
        LocalDate endDate;
        do {
            endDate = startDate.plusDays(14);
            System.out.println("Loading events for league: " + leagueId + " startdate: " + startDate + " enddate: " + endDate);
            loadEvents(startDate.toString(), endDate.toString(), leagueId, eventList);
            startDate = endDate;
        } while (endDate.isBefore(LocalDate.now()));

        if (eventList.isEmpty()) return;

        List<EventDB> eventDBList = createEventDB(eventList);
        ArrayList<IncidentDB> incidentDBList = new ArrayList<>();
        ArrayList<LineupDB> lineupDBList = new ArrayList<>();

        ArrayList<OddsDB> oddsList = new ArrayList<>();
        for (EventDB db : eventDBList) {
            loadEventStats(db);
            loadManagers(db, eventList);
            OddsDB oddsDB = loadEventOdds(db.eventId);
            if (oddsDB != null) {
                oddsList.add(oddsDB);
            }
            List<IncidentDB> incidentList = loadEventIncidents(db.eventId);
            if (incidentList != null) {
                incidentDBList.addAll(incidentList);
                Util.updateRedcards(db, incidentList);
            }

            LineupDB lineupDB = loadEventLineups(db.eventId);
            if (lineupDB != null) {
                lineupDBList.add(lineupDB);
            }
        }

        DBUtil.updateEventsCollection(database, eventDBList);
        DBUtil.updateIncidentsCollection(database, incidentDBList);
        DBUtil.updateLineupsCollection(database, lineupDBList);
        if (!oddsList.isEmpty()) {
            DBUtil.updateOddsCollection(database, oddsList);
        }
    }

    private static LocalDate getStartDate(int leagueId, MongoDatabase database) {
        String latestDBDate = getLatestEventDate(leagueId, database);
        if (latestDBDate.isEmpty()) {
            return LocalDate.parse(leagues.get(leagueId).getStartDate());
        }
        return LocalDate.parse(latestDBDate);
    }

    private static void loadManagers(EventDB db, List<SportsApiClient.Event> eventList) {
        for (SportsApiClient.Event event : eventList) {
            if (event.id == db.eventId) {
                try {
                    SportsApiClient.Manager manager = sportsApiClient.fetchManager(event.homeCoachId);
                    db.homeCoach = manager.name;
                    manager = sportsApiClient.fetchManager(event.awayCoachId);
                    db.awayCoach = manager.name;
                } catch (Exception e) {
                    System.out.println("Error when loading manager for id " + db.eventId);
                }
                break;
            }
        }
    }

    private static void loadFixtureOdds(FixtureDB fixtureDB) throws Exception {
        SportsApiClient.OddsLineResponse oddsLineResponse = sportsApiClient.fetchOdds(fixtureDB.eventId, "pinnacle");
        if (!oddsLineResponse.results.isEmpty()) {
            fixtureDB.odds = createOddsDB(oddsLineResponse.results, fixtureDB.eventId);
        }
    }

    private static void loadPredictions(FixtureDB db) throws Exception {
        SportsApiClient.Prediction response = sportsApiClient.fetchPrediction(db.eventId);
        if (response.markets != null) {
            MatchPredictionDB matchPredictionDB = new MatchPredictionDB();
            matchPredictionDB.probHome = response.markets.matchResult.probHome;
            matchPredictionDB.probDraw = response.markets.matchResult.probDraw;
            matchPredictionDB.probAway = response.markets.matchResult.probAway;
            matchPredictionDB.probOver15 = response.markets.overUnder.probOver15;
            matchPredictionDB.probOver25 = response.markets.overUnder.probOver25;
            matchPredictionDB.probOver35 = response.markets.overUnder.probOver35;
            matchPredictionDB.probYes = response.markets.btts.probYes;
            matchPredictionDB.mostLikely = response.markets.score.mostLikely;
            db.matchPrediction = matchPredictionDB;
        }
    }

    private static void loadEvents(String dateFrom, String dateTo, int leagueId, List<SportsApiClient.Event> eventList) throws Exception {
        SportsApiClient.EventsResponse response = sportsApiClient.fetchEvents(dateFrom, dateTo, leagueId);
        System.out.printf("Total events: %d%n%n", response.count);

        for (SportsApiClient.Event event : response.results) {
            System.out.println(event);
        }
        eventList.addAll(response.results);
    }

    private static void loadEventStats(EventDB eventDB) throws Exception {
        EventStats eventStats = eventStatsClient.fetchEventStats(eventDB.eventId);
        System.out.println("EventStatus for " + eventDB.eventId + " is loaded ");
        eventDB.hs = eventStats.stats.home.totalShots;
        eventDB.as = eventStats.stats.away.totalShots;
        eventDB.hy = eventStats.stats.home.yellowCards;
        eventDB.ay = eventStats.stats.away.yellowCards;
        eventDB.hc = eventStats.stats.home.cornerKicks;
        eventDB.ac = eventStats.stats.away.cornerKicks;
        eventDB.hXg = eventStats.stats.home.xg.actual;
        eventDB.aXg = eventStats.stats.away.xg.actual;
        eventDB.hPoss = eventStats.stats.home.ballPossession;
        eventDB.aPoss = eventStats.stats.away.ballPossession;
        eventDB.hFouls = eventStats.stats.home.fouls;
        eventDB.aFouls = eventStats.stats.away.fouls;
        eventDB.hBigChances = eventStats.stats.home.bigChances;
        eventDB.aBigChances = eventStats.stats.away.bigChances;
        eventDB.hSaves = eventStats.stats.home.totalSaves;
        eventDB.aSaves = eventStats.stats.away.totalSaves;
    }

    private static List<IncidentDB> loadEventIncidents(int eventId) throws Exception {
        System.out.println("\n=== Loading incidents (eventId: " + eventId + " ) ===");
        SportsApiClient.EventData eventData = sportsApiClient.fetchIncidents(eventId);
        if (!eventData.incidents.isEmpty()) {
            return createIncidentDB(eventData.incidents, eventId);
        }
        return null;
    }

    private static LineupDB loadEventLineups(int eventId) throws Exception {
        System.out.println("\n=== Loading lineups (eventId: " + eventId + " ) ===");
        SportsApiClient.LineupResponse lineupResponse = sportsApiClient.fetchLineups(eventId);
        if (lineupResponse.lineups != null) {
            return createLineupDB(lineupResponse.lineup_status, lineupResponse.lineups, eventId);
        }
        return null;
    }

    private static OddsDB loadEventOdds(int eventId) throws Exception {
        System.out.println("\n=== Loading odds (eventId: " + eventId + " ) ===");
        SportsApiClient.OddsLineResponse oddsLineResponse = sportsApiClient.fetchOdds(eventId, "pinnacle");
        if (!oddsLineResponse.results.isEmpty()) {
            return createOddsDB(oddsLineResponse.results, eventId);
        }
        return null;
    }

    static void handleRefereeData(MongoDatabase database, int leagueId) throws Exception {
        System.out.println("\n=== Loading referees (league: " + leagueId + " ) ===");
        SportsApiClient.RefereesResponse refs = sportsApiClient.fetchReferees(leagueId);
        List<RefereeDB> refereeDBList = createRefereeDB(refs.results, leagueId);

        DBUtil.updateRefereeCollection(database, refereeDBList);
    }

}

/*

loadEvents("2026-04-04", "2026-04-18", leagueId, eventList);
loadEvents("2026-04-19", "2026-05-04", leagueId, eventList);
loadEvents("2026-05-05", "2026-05-19", leagueId, eventList);
loadEvents("2026-05-20", "2026-05-31", leagueId, eventList);
loadEvents("2026-06-01", "2026-06-13", leagueId, eventList);
loadEvents("2026-06-14", "2026-06-26", leagueId, eventList);

 */