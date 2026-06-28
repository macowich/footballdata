package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import se.mac.footballdata.sportsapi.db.HeadToHeadDB;
import se.mac.footballdata.sportsapi.db.MatchPredictionDB;

@MongoEntity(collection = "fixtures")
public class Fixture extends PanacheMongoEntity
{

   public int eventId;

   public int leagueId;

   public String date;

   public String time;

   public String hometeam;

   public String awayteam;

   public String referee;

   public double homeWin;

   public double draw;

   public double awayWin;

   public double over25Goals;

   public double under25Goals;

   public HeadToHeadDB headToHead;

   public MatchPredictionDB matchPrediction;

   public Fixture()
   {
   }
}