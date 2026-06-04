package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {
    public TeamStats home;
    public TeamStats away;

    @JsonProperty("first_half")
    public HalfStats firstHalf;

    @JsonProperty("second_half")
    public HalfStats secondHalf;
}
