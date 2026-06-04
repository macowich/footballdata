package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MomentumEntry {
    /** Minute (may be fractional, e.g. 45.5 for added time) */
    public double m;

    /** Momentum value: positive = home, negative = away */
    public int v;
}
