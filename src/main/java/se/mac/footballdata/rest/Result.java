package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

@MongoEntity(collection = "results")
public class Result extends PanacheMongoEntity
{

   public String league;
   public String date;
   public String time;

   @BsonProperty("hometeam")
   public String homeTeam;

   @BsonProperty("awayteam")
   public String awayTeam;

   public String referee;

   @BsonProperty("fthg")
   public Integer fullTimeHomeGoals;

   @BsonProperty("ftag")
   public Integer fullTimeAwayGoals;

   @BsonProperty("ftr")
   public String fullTimeResult;

   @BsonProperty("hthg")
   public Integer halfTimeHomeGoals;

   @BsonProperty("htag")
   public Integer halfTimeAwayGoals;

   @BsonProperty("htr")
   public String halfTimeResult;

   @BsonProperty("hs")
   public Integer homeShots;

   @BsonProperty("as")
   public Integer awayShots;

   @BsonProperty("hst")
   public Integer homeShotsTarget;

   @BsonProperty("ast")
   public Integer awayShotsTarget;

   @BsonProperty("hc")
   public Integer homeCorners;

   @BsonProperty("ac")
   public Integer awayCorners;

   @BsonProperty("hy")
   public Integer homeYellowcards;

   @BsonProperty("ay")
   public Integer awayYellowcards;

   @BsonProperty("hr")
   public Integer homeRedcards;

   @BsonProperty("ar")
   public Integer awayRedcards;
}
