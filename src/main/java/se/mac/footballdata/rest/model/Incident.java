package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "incidents")
public class Incident extends PanacheMongoEntity {
    public int eventId;
    public int playerId;
    public String player;
    public String type;
    public String goalType;
    public String assist;
    public String cardType;
    public int minute;
    public boolean home;
    public String playerIn;
    public String playerOut;
}
