package se.mac.footballdata.sportsapi.db;

import java.util.List;

public class HeadToHeadDB {

    public int totalMatches;
    public int homeWins;
    public int draws;
    public int awayWins;
    public int homeGoals;
    public int awayGoals;
    public double avgTotalGoals;
    public double homeWinRate;
    public double awayWinRate;
    public List<RecentMatchDB> recentMatches;
}
