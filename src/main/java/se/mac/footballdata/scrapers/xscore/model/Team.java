package se.mac.footballdata.scrapers.xscore.model;

public class Team {
    public long id;
    public String name;
    public String gender;
    public long countryId;
    public String countryName;
    public Image image;
    public Integer standing; // only present for some teams

    // getters and setters
}

