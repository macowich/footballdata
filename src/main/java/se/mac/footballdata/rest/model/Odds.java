package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "odds")
public class Odds extends PanacheMongoEntity {
    public int eventId;

    public double homeWin;
    public double draw;
    public double awayWin;
    public double over15Goals;
    public double over25Goals;
    public double over35Goals;
    public double under15Goals;
    public double under25Goals;
    public double under35Goals;
    public double bttsYes;
    public double bttsNo;
}
