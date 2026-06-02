package se.mac.footballdata.predictions;

	/*
	Step 1: Calculate the average of matches with goals scored of Bilbao and goals taken by Valencia (79+79)/2=79%;
	Step 2: Calculate the average of matches with goals scored by Valencia and goals taken by Bilbao: (86+64)/2=75%;
	Step 3: Divide both percentages by 100 and you get the probabilities: 0.79 and 0.75;
	Step 4: Calculate the probability of both scenarios by multiplying these previous individual probabilities 0.79x0.75=0.5925;
	Step 5: Multiply that by 100 and you get the probability of goals by both teams 100 x 0.5925= 59.25% probability;
	Step 6: Get the odd by calculating the inverse of the probability: 1/0.5925=1.68.
	Well then, you now have the odd based on the statistical analysis, which will be the starting point: you should only bet if you can find an odd over 1.68 in a bookmaker.
	But if you want my advice, you should only bet with probabilities of over 50%.
*/

public class AvgGoalInfo {
    // Gjorda mål/match hemma (hemmalag)
    public double homegoals_team;
    // Insläppta mål/match hemma (hemmalag)
    public double homeconceded_team;

    // Gjorda mål/match borta (bortalag)
    public double awaygoals_team;
    // Insläppta mål/match borta (bortalag)
    public double awayconceded_team;

    // Gjorda mål/match hemma totalt för ligan
    public double homegoals_total;
    // Insläppta mål/match hemma totalt för ligan
    public double homeconceded_total;

    // Gjorda mål/match borta totalt för ligan
    public double awaygoals_total;
    // Insläppta mål/match borta totalt för ligan
    public double awayconceded_total;

    // Hemmalag gjorda mål/match
    public double homeTeamGoals;
    // Hemmalag insläppta mål/match
    public double homeTeamConceded;
    // Bortalag gjorda mål/match
    public double awayTeamGoals;
    // Bortalag insläppta mål/match
    public double awayTeamConceded;

    @Override
    public String toString() {
        return "AvgInfo [homegoals_team=" + homegoals_team + ", homeconceded_team=" + homeconceded_team
                + ", awaygoals_team=" + awaygoals_team + ", awayconceded_team=" + awayconceded_team
                + ", homegoals_total=" + homegoals_total + ", homeconceded_total=" + homeconceded_total
                + ", awaygoals_total=" + awaygoals_total + ", awayconceded_total=" + awayconceded_total
                + ", homeTeamGoals=" + homeTeamGoals + ", homeTeamConceded=" + homeTeamConceded + ", awayTeamGoals="
                + awayTeamGoals + ", awayTeamConceded=" + awayTeamConceded + "]";
    }
}
