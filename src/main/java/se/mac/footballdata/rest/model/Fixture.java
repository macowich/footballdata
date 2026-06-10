package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

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

   public String b365h;

   public String b365d;

   public String b365a;

   public String b365_u25;

   public String b365_o25;

   public Fixture()
   {
   }
}