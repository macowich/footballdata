package se.mac.footballdata.sportsapi.db;

import java.util.ArrayList;
import java.util.List;

public class LineupDB {

    public int eventId;
    public String homeTeam;
    public String awayTeam;
    public int homeTeamId;
    public int awayTeamId;
    public String homeFormation;
    public String awayFormation;
    public List<PlayerDB> homePlayers = new ArrayList<>();
    public List<PlayerDB> awayPlayers = new ArrayList<>();
    public List<PlayerDB> homeSubstitutes = new ArrayList<>();
    public List<PlayerDB> awaySubstitutes = new ArrayList<>();

    public LineupDB() {
        // Empty
    }

}
