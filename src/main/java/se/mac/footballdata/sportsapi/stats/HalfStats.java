package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HalfStats {
    public TeamStats home;
    public TeamStats away;
}
