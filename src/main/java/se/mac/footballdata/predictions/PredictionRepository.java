package se.mac.footballdata.predictions;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.mac.footballdata.predictions.model.Prediction;

import java.time.LocalDate;
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
        var filter = eq("eventId", prediction.eventId);
        var options = new ReplaceOptions().upsert(true);

        getCollection().replaceOne(filter, prediction, options);
    }

    public List<Prediction> listAll() {
        List<Prediction> list = new ArrayList<>();
        getCollection().find(Filters.gte("date", LocalDate.now().toString()))
                .sort(Sorts.orderBy(Sorts.ascending("date"), Sorts.ascending("time")))
                .into(list);
        return list;
    }
}