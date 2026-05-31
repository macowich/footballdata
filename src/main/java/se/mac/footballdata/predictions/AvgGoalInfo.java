package se.mac.footballdata.predictions;

public class AvgGoalInfo {
	// Gjorda m�l/match hemma (hemmalag)
	public double homegoals_team;
	// Insl�ppta m�l/match hemma (hemmalag)
	public double homeconceded_team;

	// Gjorda m�l/match borta (bortalag)
	public double awaygoals_team;
	// Insl�ppta m�l/match borta (bortalag)
	public double awayconceded_team;

	// Gjorda m�l/match hemma totalt f�r ligan
	public double homegoals_total;
	// Insl�ppta m�l/match hemma totalt f�r ligan
	public double homeconceded_total;

	// Gjorda m�l/match borta totalt f�r ligan
	public double awaygoals_total;
	// Insl�ppta m�l/match borta totalt f�r ligan
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
