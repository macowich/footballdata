package se.mac.footballdata;

import com.mongodb.MongoException;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import se.mac.footballdata.sportsapi.db.*;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    /**
     * Updates events collection in db
     *
     * @param database    Database
     * @param eventDBList List of EventDB
     */
    public static void updateEventsCollection(MongoDatabase database, List<EventDB> eventDBList) {

        MongoCollection<EventDB> collection =
                database.getCollection("events", EventDB.class);
        collection.createIndex(new Document("eventId", 1), new IndexOptions().unique(true));

        try {
            collection.insertMany(eventDBList, new InsertManyOptions().ordered(false));
            System.out.println("Inserted eventDBList: " + eventDBList);
        } catch (MongoException ex) {
            System.out.println("Error when inserting eventDBList: " + ex);
        }
    }

    /**
     * Updates odds collection in db
     *
     * @param database   Database
     * @param oddsDBList List of OddsDB
     */
    public static void updateOddsCollection(MongoDatabase database, List<OddsDB> oddsDBList) {

        MongoCollection<OddsDB> collection =
                database.getCollection("odds", OddsDB.class);
        collection.createIndex(new org.bson.Document("eventId", 1), new IndexOptions().unique(true));

        // Build bulk write list
        ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
        List<ReplaceOneModel<OddsDB>> operations = new ArrayList<>();

        for (OddsDB odds : oddsDBList) {
            operations.add(new ReplaceOneModel<>(
                    Filters.eq("eventId", odds.eventId),
                    odds,
                    replaceOptions
            ));
        }

        BulkWriteResult result = collection.bulkWrite(operations, new BulkWriteOptions().ordered(false));
        System.out.println("Upsert of odds collection complete.");
        System.out.println("Modified existing: " + result.getModifiedCount());
        System.out.println("Newly inserted: " + result.getUpserts().size());
    }

    /**
     * Updates players collection in db
     *
     * @param database     Database
     * @param playerDBList List of {@link PlayerDB}
     */
    public static void updatePlayersCollection(MongoDatabase database, List<PlayerDB> playerDBList) {
        MongoCollection<PlayerDB> collection =
                database.getCollection("players", PlayerDB.class);
        collection.createIndex(new Document("playerId", 1), new IndexOptions().unique(true));

        ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
        List<ReplaceOneModel<PlayerDB>> operations = new ArrayList<>();

        for (PlayerDB player : playerDBList) {
            operations.add(new ReplaceOneModel<>(
                    Filters.eq("playerId", player.playerId),
                    player,
                    replaceOptions
            ));
        }

        // Execute parallel bulk operations
        BulkWriteResult result = collection.bulkWrite(operations, new BulkWriteOptions().ordered(false));

        System.out.println("Upsert of player collection complete.");
        System.out.println("Modified existing: " + result.getModifiedCount());
        System.out.println("Newly inserted: " + result.getUpserts().size());
    }

    /**
     * Updates referees collection in db
     *
     * @param database      Database
     * @param refereeDBList List of {@link RefereeDB}
     */
    public static void updateRefereeCollection(MongoDatabase database, List<RefereeDB> refereeDBList) {
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

        System.out.println("Upsert of referee collection complete.");
        System.out.println("Modified existing: " + result.getModifiedCount());
        System.out.println("Newly inserted: " + result.getUpserts().size());
    }

    public static void updateIncidentsCollection(MongoDatabase database, List<IncidentDB> incidentDBList) {
        MongoCollection<IncidentDB> collection =
                database.getCollection("incidents", IncidentDB.class);
        collection.createIndex(Indexes.compoundIndex(
                Indexes.ascending("eventId"),
                Indexes.ascending("minute") // ToDo check
        ), new IndexOptions().unique(true));

        try {
            collection.insertMany(incidentDBList, new InsertManyOptions().ordered(false));
            System.out.println("Inserted incidentDBList: " + incidentDBList);
        } catch (MongoException ex) {
            System.out.println("Error when inserting incidentDBList: " + ex);
        }
    }

    public static void updateLineupsCollection(MongoDatabase database, List<LineupDB> lineupDBList) {
        MongoCollection<LineupDB> collection =
                database.getCollection("lineups", LineupDB.class);
        collection.createIndex(new Document("eventId", 1), new IndexOptions().unique(true));

        try {
            collection.insertMany(lineupDBList, new InsertManyOptions().ordered(false));
            System.out.println("Inserted lineupDBList: " + lineupDBList);
        } catch (MongoException ex) {
            System.out.println("Error when inserting lineupDBList: " + ex);
        }
    }

    /**
     * Updates fixtures collection in db
     *
     * @param database      Database
     * @param fixtureDBList List of {@link FixtureDB}
     */
    public static void updateFixturesCollection(MongoDatabase database, List<FixtureDB> fixtureDBList) {
        MongoCollection<FixtureDB> collection =
                database.getCollection("fixtures", FixtureDB.class);
        collection.createIndex(new Document("eventId", 1), new IndexOptions().unique(true));

        // Build bulk write list
        ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
        List<ReplaceOneModel<FixtureDB>> operations = new ArrayList<>();

        for (FixtureDB fix : fixtureDBList) {
            operations.add(new ReplaceOneModel<>(
                    Filters.eq("eventId", fix.eventId),
                    fix,
                    replaceOptions
            ));
        }

        BulkWriteResult result = collection.bulkWrite(operations, new BulkWriteOptions().ordered(false));
        System.out.println("Upsert of fixtures collection complete.");
        System.out.println("Modified existing: " + result.getModifiedCount());
        System.out.println("Newly inserted: " + result.getUpserts().size());
    }

}
