package se.mac.footballdata.sportsapi.db;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import se.mac.footballdata.DBUtil;
import se.mac.footballdata.sportsapi.SportsApiClient;
import se.mac.footballdata.sportsapi.stats.EventStats;
import se.mac.footballdata.sportsapi.stats.EventStatsClient;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class SportsApiLoader {
    private final static String DB_CONNECTION_STRING = "mongodb://localhost:27017";

    private static final SportsApiClient sportsApiClient = new SportsApiClient();
    private static final EventStatsClient eventStatsClient = new EventStatsClient();

    public static void main(String[] args) {
        // Enable POJO codec support
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

            MongoCollection<EventDB> collection =
                    database.getCollection("events", EventDB.class);
            collection.createIndex(new Document("eventId", 1), new IndexOptions().unique(true));

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

        List<SportsApiClient.Event> eventList = new ArrayList<>();
        SportsApiClient.EventsResponse response = sportsApiClient.
                fetchEvents("2026-06-08", "2026-07-04", leagueId, "notstarted");

        for (SportsApiClient.Event event : response.results) {
            System.out.println(event.id);
            System.out.println(event);
        }
        eventList.addAll(response.results);

        List<FixtureDB> fixtureDBList = createFixtureDB(eventList);
        for (FixtureDB db : fixtureDBList) {
            loadFixtureOdds(db);
        }

        try {
            collection.insertMany(fixtureDBList, new InsertManyOptions().ordered(false));
            System.out.println("Inserted: " + fixtureDBList);
        } catch (MongoException ex) {
            System.out.println("Error: " + ex);
        }

    }

    static void handleEventsData(MongoDatabase database, int leagueId) throws Exception {
        MongoCollection<EventDB> collection =
                database.getCollection("events", EventDB.class);

        List<SportsApiClient.Event> eventList = new ArrayList<>();
        loadEvents("2026-04-04", "2026-04-18", leagueId, eventList);
        loadEvents("2026-04-19", "2026-05-04", leagueId, eventList);
        loadEvents("2026-05-05", "2026-05-19", leagueId, eventList);
        loadEvents("2026-05-20", "2026-05-31", leagueId, eventList);
        loadEvents("2026-06-01", "2026-06-13", leagueId, eventList);
        loadEvents("2026-06-14", "2026-06-17", leagueId, eventList);
        List<EventDB> eventDBList = createEventDB(eventList);

        ArrayList<OddsDB> oddsList = new ArrayList<>();
        for (EventDB db : eventDBList) {
            loadEventStats(db);
            OddsDB oddsDB = loadEventOdds(db.eventId);
            if (oddsDB != null) {
                oddsList.add(oddsDB);
            }
        }


        // Util
        try {
            collection.insertMany(eventDBList, new InsertManyOptions().ordered(false));
            System.out.println("Inserted eventDBList: " + eventDBList);
        } catch (MongoException ex) {
            System.out.println("Error when inserting eventDBList: " + ex);
        }

        DBUtil.updateOddsCollection(database, oddsList);
    }

    private static List<FixtureDB> createFixtureDB(List<SportsApiClient.Event> eventList) {
        return eventList.stream()
                .map(SportsApiLoader::createFixtureDB)
                .toList();
    }

    private static FixtureDB createFixtureDB(SportsApiClient.Event event) {
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

    private static void loadFixtureOdds(FixtureDB fixtureDB) throws Exception {
        SportsApiClient.EventOdds eventOdds = sportsApiClient.fetchEventOdds(fixtureDB.eventId);
        System.out.printf("Event ID: %d%n", fixtureDB.eventId);
        fixtureDB.homeWin = eventOdds.odds.homeWin;
        fixtureDB.draw = eventOdds.odds.draw;
        fixtureDB.awayWin = eventOdds.odds.awayWin;
        fixtureDB.over25Goals = eventOdds.odds.over25Goals;
        fixtureDB.under25Goals = eventOdds.odds.under25Goals;
    }

    private static List<EventDB> createEventDB(List<SportsApiClient.Event> eventList) {
        return eventList.stream()
                .map(SportsApiLoader::createEventDB)
                .toList();
    }

    private static EventDB createEventDB(SportsApiClient.Event event) {
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

    private static OddsDB loadEventOdds(int eventId) throws Exception {
        System.out.println("\n=== Loading odds (eventId: " + eventId + " ) ===");
        SportsApiClient.OddsLineResponse oddsLineResponse = sportsApiClient.fetchOdds(eventId, "pinnacle");
        if (!oddsLineResponse.results.isEmpty()) {
            return createOddsDB(oddsLineResponse.results, eventId);
        }
        return null;
    }

    /*
    private static List<OddsDB> createOddsDB(List<SportsApiClient.EventOdds> eventOddsList, int leagueId) {
        return eventOddsList.stream()
                .map(odds -> createOddsDB(odds, leagueId))
                .toList();
    }*/

    private static OddsDB createOddsDB(List<SportsApiClient.OddsLine> results, int eventId) {
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

    static void handleRefereeData(MongoDatabase database, int leagueId) throws Exception {
        System.out.println("\n=== Loading referees (league: " + leagueId + " ) ===");
        SportsApiClient.RefereesResponse refs = sportsApiClient.fetchReferees(leagueId);
        List<RefereeDB> refereeDBList = createRefereeDB(refs.results, leagueId);

        MongoCollection<RefereeDB> collection =
                database.getCollection("referees", RefereeDB.class);
        collection.createIndex(new Document("refereeId", 1), new IndexOptions().unique(true));

        // Build bulk write list
        ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
        List<ReplaceOneModel<RefereeDB>> operations = new ArrayList<>();

        for (RefereeDB ref : refereeDBList) {
            operations.add(new ReplaceOneModel<>(
                    Filters.eq("refereeId", ref.refereeId),
                    ref,
                    replaceOptions
            ));
        }

        // Execute parallel bulk operations
        BulkWriteResult result = collection.bulkWrite(operations, new BulkWriteOptions().ordered(false));

        System.out.println("Upsert complete.");
        System.out.println("Modified existing: " + result.getModifiedCount());
        System.out.println("Newly inserted: " + result.getUpserts().size());
    }

    private static List<RefereeDB> createRefereeDB(List<SportsApiClient.Referee> refereeList, int leagueId) {
        return refereeList.stream()
                .map(referee -> createRefereeDB(referee, leagueId))
                .toList();
    }

    private static RefereeDB createRefereeDB(SportsApiClient.Referee referee, int leagueId) {
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