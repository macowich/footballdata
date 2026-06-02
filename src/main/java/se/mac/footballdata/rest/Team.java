package se.mac.footballdata.rest;

public class Team
{
   public String name;
   public int matches;

   public int goals; // Gjorda mål
   public int conceeded; // Insläppta mål
   public double avgGoals; // Average

   public int homeMatches;
   public int awayMatches;
   public int goalshome;
   public int conceededhome;
   public int goalsaway;
   public int conceededaway;

   public double avgGoalsHome;
   public double avgGoalsAway;

   public double goalsHomeTeam; // homegoals_team
   public double concededHomeTeam; // homeconceded_team
   public double goalsAwayTeam; // awaygoals_team
   public double concededAwayTeam; // awayconceded_team

   public double goalsHomeLeague; // Samma som concededAwayLeague
   public double concededHomeLeague; // Samma som goalsAwayLeague

   public int over2;

}
