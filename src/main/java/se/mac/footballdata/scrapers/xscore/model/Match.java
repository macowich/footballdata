package se.mac.footballdata.scrapers.xscore.model;

import java.util.List;

public class Match {
    public long id;
    public String start;
    public String statusType;
    public int correctionTime;
    public int statusId;
    public String statusName;
    public String statusShortName;
    public String roundName;
    public long stageId;
    public String stageName;
    public String stageStart;
    public String stageEnd;
    public String gender;
    public long tournamentId;
    public String tournamentName;
    public long templateId;
    public String templateName;
    public long countryId;
    public String countryName;
    public String winner;
    public int homeYellowCards;
    public int homeRedCards;
    public int awayYellowCards;
    public int awayRedCards;
    public boolean isKnockoutRound;
    public boolean hasLigatable;
    public boolean hasLiveLeagueTable;

    public List<Team> home;
    public List<Team> away;
    public List<Result> results;

    // getters and setters
}

