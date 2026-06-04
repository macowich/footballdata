package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XgPerMinute {
    /** Minute */
    public double m;

    @JsonProperty("xg_home")
    public double xgHome;

    @JsonProperty("xg_away")
    public double xgAway;

    @JsonProperty("cum_home")
    public double cumulativeHome;

    @JsonProperty("cum_away")
    public double cumulativeAway;
}
