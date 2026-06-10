package se.mac.footballdata.predictions.model;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "predictions")
public class Prediction {

    public int eventId;

    public String date;

    public String time;

    public String hometeam;

    public String awayteam;

    public String homeWin;

    public String draw;

    public String awayWin;

    public String u25;

    public String o25;
}
