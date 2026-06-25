package se.mac.footballdata.sportsapi.db;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
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
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
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

             //handleFixturesData(database, 26);
            handleLeague(database, 26);
            //handleLeague(database, 55);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void handleLeague(MongoDatabase database, int leagueId) throws Exception {
        handleEventsData(database, leagueId);
        //handleRefereeData(database, leagueId);
    }

    static void handleFixturesData(MongoDatabase database, int leagueId) throws Exception {
        MongoCollection<FixtureDB> collection =
                database.getCollection("fixtures", FixtureDB.class);
        collection.createIndex(new Document("eventId", 1), new IndexOptions().unique(true));

        String currentDate = LocalDate.now().toString();
        System.out.println("Loading fixtures for league" + leagueId + " from " + currentDate);
        SportsApiClient.EventsResponse response = sportsApiClient.
                fetchEvents(currentDate, "2026-07-04", leagueId, "notstarted");

        for (SportsApiClient.Event event : response.results) {
            System.out.println(event.id);
            System.out.println(event);
        }

        List<SportsApiClient.Event> eventList = new ArrayList<>(response.results);

        List<FixtureDB> fixtureDBList = createFixtureDB(eventList);
        for (FixtureDB db : fixtureDBList) {
            loadFixtureOdds(db);
        }

        try {
            collection.insertMany(fixtureDBList, new InsertManyOptions().ordered(false));
            System.out.println("Inserted fixtures: " + fixtureDBList);
        } catch (MongoException ex) {
            System.out.println("Error when inserting fixtures: " + ex);
        }
    }

    static void handleEventsData(MongoDatabase database, int leagueId) throws Exception {
        List<SportsApiClient.Event> eventList = new ArrayList<>();
        loadEvents("2026-04-04", "2026-04-18", leagueId, eventList);
        loadEvents("2026-04-19", "2026-05-04", leagueId, eventList);
        loadEvents("2026-05-05", "2026-05-19", leagueId, eventList);
        loadEvents("2026-05-20", "2026-05-31", leagueId, eventList);
        loadEvents("2026-06-01", "2026-06-13", leagueId, eventList);
        loadEvents("2026-06-14", "2026-06-23", leagueId, eventList);
        List<EventDB> eventDBList = createEventDB(eventList);

        ArrayList<IncidentDB> incidentDBList = new ArrayList<>();
        ArrayList<LineupDB> lineupDBList = new ArrayList<>();

        ArrayList<OddsDB> oddsList = new ArrayList<>();
        for (EventDB db : eventDBList) {
            loadEventStats(db);
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
       DBUtil.updateOddsCollection(database, oddsList);
    }

    private static void loadFixtureOdds(FixtureDB fixtureDB) throws Exception {
        SportsApiClient.EventOdds eventOdds = sportsApiClient.fetchEventOdds(fixtureDB.eventId);
        fixtureDB.homeWin = eventOdds.odds.homeWin;
        fixtureDB.draw = eventOdds.odds.draw;
        fixtureDB.awayWin = eventOdds.odds.awayWin;
        fixtureDB.over25Goals = eventOdds.odds.over25Goals;
        fixtureDB.under25Goals = eventOdds.odds.under25Goals;
    }

    private static void loadEvents(String dateFrom, String dateTo, int leagueId, List<SportsApiClient.Event> eventList) throws Exception {
        SportsApiClient.EventsResponse response = sportsApiClient.fetchEvents(dateFrom, dateTo, leagueId);
        System.out.printf("Total events: %d%n%n", response.count);

        for (SportsApiClient.Event event : response.results) {
            System.out.println(event.id);
            System.out.println(event);
        }
        eventList.addAll(response.results);
    }

    private static void loadEventStats(EventDB eventDB) throws Exception {
        EventStats eventStats = eventStatsClient.fetchEventStats(eventDB.eventId);
        System.out.println("EventStatus for " + eventDB.eventId + " is loaded " + eventStats.eventId);
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
            return createLineupDB(lineupResponse.lineups, eventId);
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

    private static String getLatestFixtureDate(int leagueId, MongoCollection<FixtureDB> collection) {
        FixtureDB fixtureDB = collection.aggregate(Arrays.asList(
                match(eq("leagueId", leagueId)),
                sort(descending("date")),
                limit(1),
                project(fields(include("date"), excludeId()))
        )).first();
        return fixtureDB != null ? fixtureDB.date : "";
    }

}