package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "namnlista") // Valfritt: sätter namnet på din kollektion
public class LagKombination extends PanacheMongoEntity {

    // id-fältet (ObjectId) ärvs automatiskt från PanacheMongoEntity

    public String namn1;
    public String namn2;
    public String odds;
    public String odds2;

    // Tom konstruktor (krävs)
    public LagKombination() {
    }

}

