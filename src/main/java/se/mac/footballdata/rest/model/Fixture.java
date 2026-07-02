package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import se.mac.footballdata.sportsapi.db.HeadToHeadDB;
import se.mac.footballdata.sportsapi.db.MatchPredictionDB;
import se.mac.footballdata.sportsapi.db.OddsDB;

@MongoEntity(collection = "fixtures")
public class Fixture extends PanacheMongoEntity
{

   public int eventId;

   public int leagueId;

   public String venue;

   public String date;

   public String time;

   public String hometeam;

   public String awayteam;

   public int refereeId;

   public String referee;

   public OddsDB odds;

   public HeadToHeadDB headToHead;

   public MatchPredictionDB matchPrediction;

   public Fixture()
   {
   }
}