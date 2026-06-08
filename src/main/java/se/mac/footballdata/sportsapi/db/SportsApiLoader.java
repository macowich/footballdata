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

            handleEventsData(database);
            handleRefereeData(database);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void handleEventsData(MongoDatabase database) throws Exception {
        MongoCollection<EventDB> collection =
                database.getCollection("events", EventDB.class);

        collection.createIndex(new Document("eventId", 1), new IndexOptions().unique(true));

        List<SportsApiClient.Event> eventList = new ArrayList<>();
        loadEvents("2026-04-04", "2026-04-18", 26, eventList);
        loadEvents("2026-04-19", "2026-05-04", 26, eventList);
        loadEvents("2026-05-05", "2026-05-19", 26, eventList);
        loadEvents("2026-05-20", "2026-05-31", 26, eventList);
        List<EventDB> eventDBList = createEventDB(eventList);
        for (EventDB db : eventDBList) {
            loadEventStats(db);
        }

        try {
            collection.insertMany(eventDBList, new InsertManyOptions().ordered(false));
            System.out.println("Inserted: " + eventDBList);
        } catch (MongoException ex) {
            System.out.println("Error: " + ex);
        }

        // Read back
        EventDB result = collection.find().first();
        assert result != null;
        System.out.println("Read from MongoDB: " + result.eventId);
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
    }

    static void handleRefereeData(MongoDatabase database) throws Exception {

        // ── Referees ──────────────────────────────────
        System.out.println("\n=== Loading referees (league 26) ===");
        SportsApiClient.RefereesResponse refs = sportsApiClient.fetchReferees(26);
        List<RefereeDB> refereeDBList = createRefereeDB(refs.results, 26);

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