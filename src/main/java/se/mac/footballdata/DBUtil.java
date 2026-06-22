package se.mac.footballdata;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import se.mac.footballdata.sportsapi.db.OddsDB;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {


    /**
     * Updates odds collection in db
     *
     * @param database Database
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

}
