package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "fixtures")
public class Fixture extends PanacheMongoEntity {

    public String key;

    public String league;

    public String date;

    public String time;

    public String hometeam;

    public String awayteam;

    public String referee;

    public Double b365h;

    public Double b365d;

    public Double b365a;


    public Fixture() {
    }
}