package se.mac.footballdata.predictions.model;

public class Team {

    private String name;

    private int numberOfHomeMatches;
    private int numberOfAwayMatches;

    private int homeGoalsScored;
    private int awayGoalsScored;
    private int homeGoalsConceded;
    private int awayGoalsConceded;
    private int homeCorners;
    private int awayCorners;
    private int totalCorners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void numberOfHomeMatches() {
        numberOfHomeMatches++;
    }

    public void numberOfAwayMatches() {
        numberOfAwayMatches++;
    }

    public int getNumberOfHomeMatches() {
        return numberOfHomeMatches;
    }

    public int getNumberOfAwayMatches() {
        return numberOfAwayMatches;
    }

    public int getHomeGoalsScored() {
        return homeGoalsScored;
    }

    public void setHomeGoalsScored(int homeGoalsScored) {
        this.homeGoalsScored += homeGoalsScored;
    }

    public int getAwayGoalsScored() {
        return awayGoalsScored;
    }

    public void setAwayGoalsScored(int awayGoalsScored) {
        this.awayGoalsScored += awayGoalsScored;
    }

    public int getHomeGoalsConceded() {
        return homeGoalsConceded;
    }

    public void setHomeGoalsConceded(int homeGoalsConceded) {
        this.homeGoalsConceded += homeGoalsConceded;
    }

    public int getAwayGoalsConceded() {
        return awayGoalsConceded;
    }

    public void setAwayGoalsConceded(int awayGoalsConceded) {
        this.awayGoalsConceded += awayGoalsConceded;
    }

    public double goalsScored() {
        return homeGoalsScored + awayGoalsScored;
    }

    public double goalsConceded() {
        return homeGoalsConceded + awayGoalsConceded;
    }

    public int getHomeCorners() {
        return homeCorners;
    }

    public void setHomeCorners(int homeCorners) {
        this.homeCorners += homeCorners;
    }

    public int getAwayCorners() {
        return awayCorners;
    }

    public void setAwayCorners(int awayCorners) {
        this.awayCorners += awayCorners;
    }

    public void setTotalCorners(int corners) {
        this.totalCorners += corners;
    }

    public int getTotalCorners() {
        return totalCorners;
    }

    @Override
    public String toString() {
        return "Team [name=" + name + ", numberOfHomeMatches=" + numberOfHomeMatches + ", numberOfAwayMatches="
                + numberOfAwayMatches + ", homeGoalsScored=" + homeGoalsScored + ", awayGoalsScored=" + awayGoalsScored
                + ", homeGoalsConceded=" + homeGoalsConceded + ", awayGoalsConceded=" + awayGoalsConceded
                + ", homeCorners=" + homeCorners + ", awayCorners=" + awayCorners + ", totalCorners=" + totalCorners
                + "]";
    }

}
