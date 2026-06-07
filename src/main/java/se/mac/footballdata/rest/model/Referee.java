package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "referees")
public class Referee extends PanacheMongoEntity {
    public int refereeId;
    public String name;
    public int matches;
    public int totalYellowCards;
    public int totalRedCards;
    public double avgYellowPerMatch;
    public double avgRedPerMatch;

}
