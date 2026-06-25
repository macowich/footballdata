package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;
import java.util.List;

@MongoEntity(collection = "lineups")
public class Lineup {

    public ObjectId id;

    public String awayFormation;
    public List<Player> awayPlayers;
    public List<Player> awaySubstitutes;
    public String awayTeam;
    public int awayTeamId;

    public int eventId;

    public String homeFormation;
    public List<Player> homePlayers;
    public List<Player> homeSubstitutes;
    public String homeTeam;
    public int homeTeamId;
}

