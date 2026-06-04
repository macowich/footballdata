package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamStats {

    public int duels;
    public int fouls;
    public int passes;
    public int punches;
    public int tackles;
    public int offsides;

    @JsonProperty("big_saves")
    public int bigSaves;

    @JsonProperty("throw_ins")
    public int throwIns;

    public int clearances;

    @JsonProperty("free_kicks")
    public int freeKicks;

    @JsonProperty("goal_kicks")
    public int goalKicks;

    public int recoveries;

    @JsonProperty("big_chances")
    public int bigChances;

    @JsonProperty("high_claims")
    public int highClaims;

    @JsonProperty("tackles_won")
    public int tacklesWon;

    @JsonProperty("total_saves")
    public int totalSaves;

    @JsonProperty("total_shots")
    public int totalShots;

    @JsonProperty("corner_kicks")
    public int cornerKicks;

    public int dispossessed;

    @JsonProperty("hit_woodwork")
    public int hitWoodwork;

    @JsonProperty("yellow_cards")
    public int yellowCards;

    @JsonProperty("blocked_shots")
    public int blockedShots;

    public int interceptions;

    @JsonProperty("through_balls")
    public int throughBalls;

    @JsonProperty("total_tackles")
    public int totalTackles;

    @JsonProperty("expected_goals")
    public double expectedGoals;

    @JsonProperty("accurate_passes")
    public int accuratePasses;

    @JsonProperty("ball_possession")
    public int ballPossession;

    @JsonProperty("goals_prevented")
    public double goalsPrevented;

    @JsonProperty("shots_on_target")
    public int shotsOnTarget;

    @JsonProperty("goalkeeper_saves")
    public int goalkeeperSaves;

    @JsonProperty("shots_inside_box")
    public int shotsInsideBox;

    @JsonProperty("shots_off_target")
    public int shotsOffTarget;

    @JsonProperty("shots_outside_box")
    public int shotsOutsideBox;

    @JsonProperty("big_chances_missed")
    public int bigChancesMissed;

    @JsonProperty("big_chances_scored")
    public int bigChancesScored;

    @JsonProperty("final_third_entries")
    public int finalThirdEntries;

    @JsonProperty("errors_lead_to_a_goal")
    public int errorsLeadToGoal;

    @JsonProperty("errors_lead_to_a_shot")
    public int errorsLeadToShot;

    @JsonProperty("fouled_in_final_third")
    public int fouledInFinalThird;

    @JsonProperty("touches_in_penalty_area")
    public int touchesInPenaltyArea;

    public int attack;

    @JsonProperty("ball_safe")
    public int ballSafe;

    @JsonProperty("dangerous_attack")
    public int dangerousAttack;

    @JsonProperty("attack_pct")
    public int attackPct;

    @JsonProperty("ball_safe_pct")
    public int ballSafePct;

    @JsonProperty("dangerous_attack_pct")
    public int dangerousAttackPct;

    @JsonProperty("pass_accuracy_pct")
    public double passAccuracyPct;

    public Crosses crosses;
    public Dribbles dribbles;

    @JsonProperty("long_balls")
    public LongBalls longBalls;

    @JsonProperty("aerial_duels")
    public AerialDuels aerialDuels;

    @JsonProperty("ground_duels")
    public GroundDuels groundDuels;

    @JsonProperty("final_third_phase")
    public FinalThirdPhase finalThirdPhase;

    public Xg xg;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Crosses {
        public int value;
        public int total;
        public int pct;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dribbles {
        public int value;
        public int total;
        public int pct;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LongBalls {
        public int value;
        public int total;
        public int pct;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AerialDuels {
        public int value;
        public int total;
        public int pct;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GroundDuels {
        public int value;
        public int total;
        public int pct;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FinalThirdPhase {
        public int value;
        public int total;
        public int pct;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Xg {
        public double actual;
    }
}
