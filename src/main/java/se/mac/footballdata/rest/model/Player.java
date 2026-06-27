package se.mac.footballdata.rest.model;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "players")
public class Player {
    public int jerseyNumber;
    public String name;
    public int playerId;
    public int teamId;
    public int leagueId;
    public String position;
    public int rating;
    public String nationality;
}

