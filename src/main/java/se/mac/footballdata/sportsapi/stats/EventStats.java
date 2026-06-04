package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventStats {

    @JsonProperty("event_id")
    public int eventId;

    public Stats stats;

    public List<ShotMapEntry> shotmap;

    public List<MomentumEntry> momentum;

    @JsonProperty("average_positions")
    public AveragePositions averagePositions;

    @JsonProperty("xg_per_minute")
    public List<XgPerMinute> xgPerMinute;
}
