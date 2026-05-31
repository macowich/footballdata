package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "fixtures")
public class Fixture extends PanacheMongoEntity {

    public String fixture_id;

    public String league;

    public String date;

    public String time;

    public String hometeam;

    public String awayteam;

    public String referee;

    public String b365h;

    public String b365d;

    public String b365a;

    public String b365_u25;

    public String b365_o25;


    public Fixture() {
    }
}