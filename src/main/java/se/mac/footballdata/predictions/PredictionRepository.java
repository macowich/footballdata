package se.mac.footballdata.predictions;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.mac.footballdata.predictions.model.Prediction;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class PredictionRepository {

    @Inject
    MongoClient mongoClient;

    private MongoCollection<Prediction> getCollection() {
        return mongoClient.getDatabase("sportsdb")
                .getCollection("predictions", Prediction.class);
    }

    public void saveOrOverwrite(Prediction prediction) {
        // Query by "_id" because MongoDB treats your @BsonId field as the core identifier
        var filter = eq("fixture_id", prediction.fixture_id);
        var options = new ReplaceOptions().upsert(true);

        getCollection().replaceOne(filter, prediction, options);
    }

    public List<Prediction> listAll() {
        List<Prediction> list = new ArrayList<>();
        getCollection().find().into(list);
        return list;
    }
}