package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShotMapEntry {

    public double xg;
    public String gml;
    public int min;
    public String sit;
    public String body;
    public boolean home;
    public String type;
    public Double xgot;
    public Integer added;
    public String gtype;

    @JsonProperty("player_id")
    public int playerId;

    /** Goal-mouth position */
    public Coordinate gm;

    /** Shot position on pitch */
    public Coordinate pos;

    /** Block position (if blocked) */
    public Coordinate block;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coordinate {
        public double x;
        public double y;
        public double z;
    }
}
