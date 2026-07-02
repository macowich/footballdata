package se.mac.footballdata.sportsapi.db;

public class FixtureDB {
    public int eventId;
    public int leagueId;
    public int seasonId;
    public String venue;
    public String date;
    public String time;
    public String hometeam;
    public String awayteam;
    public int refereeId;
    public OddsDB odds;
    public HeadToHeadDB headToHead;
    public MatchPredictionDB matchPrediction;

    public FixtureDB() {

    }

}
