package se.mac.footballdata.predictions.model;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "predictions")
public class Prediction {

    public String fixture_id;

    public String homewin;

    public String draw;

    public String awaywin;

    public String u25;

    public String o25;
}
