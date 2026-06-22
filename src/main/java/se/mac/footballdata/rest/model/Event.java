package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

@MongoEntity(collection = "events")
public class Event extends PanacheMongoEntity {
    public int eventId;
    public String date;
    public String time;

    @BsonProperty("hometeam")
    public String homeTeam;

    @BsonProperty("awayteam")
    public String awayTeam;

    public String referee;

    public Integer refereeId;

    public Integer round;

    @BsonProperty("homeScore")
    public Integer fullTimeHomeGoals;

    @BsonProperty("awayScore")
    public Integer fullTimeAwayGoals;

    @BsonProperty("homeScoreHt")
    public Integer halfTimeHomeGoals;

    @BsonProperty("awayScoreHt")
    public Integer halfTimeAwayGoals;

    @BsonProperty("hs")
    public Integer homeShots;

    @BsonProperty("as")
    public Integer awayShots;

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

    @BsonProperty("hXg")
    public Double homeXg;

    @BsonProperty("aXg")
    public Double awayXg;

    @BsonProperty("hPoss")
    public Integer homePossesion;

    @BsonProperty("aPoss")
    public Integer awayPossesion;

    @BsonProperty("hSaves")
    public Integer homeSaves;

    @BsonProperty("aSaves")
    public Integer awaySaves;

    @BsonProperty("hFouls")
    public Integer homeFouls;

    @BsonProperty("aFouls")
    public Integer awayFouls;

    @BsonProperty("hBigChances")
    public Integer homeBigChances;

    @BsonProperty("aBigChances")
    public Integer awayBigChances;

    public Odds odds;
}
