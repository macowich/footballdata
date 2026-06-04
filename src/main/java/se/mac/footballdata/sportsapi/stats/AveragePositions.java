package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AveragePositions {
    public List<PlayerPosition> home;
    public List<PlayerPosition> away;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerPosition {
        /** Shirt number */
        public int n;

        /** Average x position on pitch */
        public double x;

        /** Average y position on pitch */
        public double y;

        @JsonProperty("pid")
        public long playerId;

        /** Position abbreviation: G, D, M, F */
        public String pos;

        public String name;
    }
}
