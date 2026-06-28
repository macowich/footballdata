package se.mac.footballdata.sportsapi.db;

public class FixtureDB {
    public int eventId;
    public int leagueId;
    public int seasonId;
    public String date;
    public String time;
    public String hometeam;
    public String awayteam;
    public int refereeId;
    public double homeWin;
    public double draw;
    public double awayWin;
    public double over25Goals;
    public double under25Goals;

    public HeadToHeadDB headToHead;
    public MatchPredictionDB matchPrediction;

    public FixtureDB() {

    }

}
