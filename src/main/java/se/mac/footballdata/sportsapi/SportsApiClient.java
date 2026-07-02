package se.mac.footballdata.sportsapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public class SportsApiClient {

    private static final String BASE_URL = "https://sports.bzzoiro.com/api/v2/events/";
    private static final String BASE_URL_REFEREES = "https://sports.bzzoiro.com/api/v2/referees/";
    private static final String BASE_URL_PLAYERS = "https://sports.bzzoiro.com/api/v2/players/";
    private static final String BASE_URL_VENUES = "https://sports.bzzoiro.com/api/v2/venues/";
    private static final String BASE_URL_MANAGERS = "https://sports.bzzoiro.com/api/v2/managers/";
    private static final String BASE_URL_TEAMS = "https://sports.bzzoiro.com/api/v2/teams/";
    private static final String BASE_URL_PREDICTIONS = "https://sports.bzzoiro.com/api/v2/predictions/";
    private static final String BASE_URL_ODDS = "https://sports.bzzoiro.com/api/v2/odds/";
    private static final String API_TOKEN = "bb387466704c69d4660a51d47153ce12f6a1c433";

    // ──────────────────────────────────────────────
    // Model classes
    // ──────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventsResponse {
        public int count;
        public String next;
        public String previous;
        public List<Event> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event {
        public int id;
        @JsonProperty("league_id")
        public int leagueId;
        @JsonProperty("season_id")
        public int seasonId;
        @JsonProperty("home_team_id")
        public int homeTeamId;
        @JsonProperty("home_team")
        public String homeTeam;
        @JsonProperty("away_team_id")
        public int awayTeamId;
        @JsonProperty("away_team")
        public String awayTeam;
        @JsonProperty("home_coach_id")
        public Integer homeCoachId;
        @JsonProperty("away_coach_id")
        public Integer awayCoachId;
        @JsonProperty("referee_id")
        public Integer refereeId;
        @JsonProperty("venue_id")
        public Integer venueId;
        @JsonProperty("event_date")
        public OffsetDateTime eventDate;
        public String status;
        @JsonProperty("replaced_by")
        public Integer replacedBy;
        @JsonProperty("round_number")
        public int roundNumber;
        @JsonProperty("round_name")
        public String roundName;
        @JsonProperty("group_name")
        public String groupName;
        public String period;
        @JsonProperty("current_minute")
        public int currentMinute;
        @JsonProperty("home_score")
        public int homeScore;
        @JsonProperty("away_score")
        public int awayScore;
        @JsonProperty("home_score_ht")
        public int homeScoreHt;
        @JsonProperty("away_score_ht")
        public int awayScoreHt;
        @JsonProperty("penalty_shootout")
        public Object penaltyShootout;
        @JsonProperty("extra_time_score")
        public Object extraTimeScore;
        @JsonProperty("is_local_derby")
        public boolean isLocalDerby;
        @JsonProperty("is_neutral_ground")
        public boolean isNeutralGround;
        @JsonProperty("travel_distance_km")
        public double travelDistanceKm;
        public Weather weather;
        @JsonProperty("pitch_condition")
        public int pitchCondition;
        public Integer attendance;
        @JsonProperty("live_websocket")
        public boolean liveWebsocket;
        public List<Highlight> highlights;
        @JsonProperty("head_to_head")
        public HeadToHead headToHead;

        @Override
        public String toString() {
            return String.format("[%s] %s %d-%d %s (Round %d)",
                    eventDate, homeTeam, homeScore, awayScore, awayTeam, roundNumber);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        public int code;
        public String description;
        @JsonProperty("wind_speed")
        public double windSpeed;
        @JsonProperty("temperature_c")
        public double temperatureC;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Highlight {
        public String kind;
        public String title;
        public String url;
        public String thumbnail;
        @JsonProperty("published_at")
        public OffsetDateTime publishedAt;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeadToHead {
        @JsonProperty("total_matches")
        public int totalMatches;
        @JsonProperty("home_wins")
        public int homeWins;
        public int draws;
        @JsonProperty("away_wins")
        public int awayWins;
        @JsonProperty("home_goals")
        public int homeGoals;
        @JsonProperty("away_goals")
        public int awayGoals;
        @JsonProperty("avg_total_goals")
        public double avgTotalGoals;
        @JsonProperty("home_win_rate")
        public double homeWinRate;
        @JsonProperty("away_win_rate")
        public double awayWinRate;
        @JsonProperty("recent_matches")
        public List<RecentMatch> recentMatches;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecentMatch {
        public String home;
        public String away;
        public String score;
        public OffsetDateTime date;
    }


    // ──────────────────────────────────────────────
    // Referee model classes
    // ──────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefereesResponse {
        public int count;
        public String next;
        public String previous;
        public List<Referee> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Referee {
        public int id;
        public String name;
        public String country;
        @JsonProperty("nationality_a3")
        public String nationalityA3;
        public String birthdate;
        /**
         * Matches refereed in the queried league/season.
         */
        public int matches;
        @JsonProperty("total_yellow_cards")
        public int totalYellowCards;
        @JsonProperty("total_red_cards")
        public int totalRedCards;
        @JsonProperty("avg_yellow_per_match")
        public double avgYellowPerMatch;
        @JsonProperty("avg_red_per_match")
        public double avgRedPerMatch;
        @JsonProperty("avg_goals_per_match")
        public double avgGoalsPerMatch;
        @JsonProperty("avg_fouls_per_match")
        public double avgFoulsPerMatch;
        /**
         * All-time career statistics.
         */
        @JsonProperty("career_games")
        public int careerGames;
        @JsonProperty("career_yellow_cards")
        public int careerYellowCards;
        @JsonProperty("career_red_cards")
        public int careerRedCards;

        @Override
        public String toString() {
            return String.format("%-22s | matches %2d | Y/game %.2f | R/game %.2f | goals/game %.2f",
                    name, matches, avgYellowPerMatch, avgRedPerMatch, avgGoalsPerMatch);
        }
    }

    // ──────────────────────────────────────────────
    // Player model classes
    // ──────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayersResponse {
        public int count;
        public String next;
        public String previous;
        public List<Player> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Player {
        public int id;
        public String name;
        @JsonProperty("short_name")
        public String shortName;
        /**
         * Primary position code: GK / D / M / F
         */
        public String position;
        @JsonProperty("specific_position")
        public String specificPosition;
        @JsonProperty("jersey_number")
        public Integer jerseyNumber;
        @JsonProperty("date_of_birth")
        public String dateOfBirth;
        @JsonProperty("height_cm")
        public Integer heightCm;
        @JsonProperty("weight_kg")
        public Integer weightKg;
        @JsonProperty("preferred_foot")
        public String preferredFoot;
        public String nationality;
        @JsonProperty("current_team_id")
        public Integer currentTeamId;
        @JsonProperty("national_team_id")
        public Integer nationalTeamId;
        @JsonProperty("market_value_eur")
        public Long marketValueEur;
        @JsonProperty("contract_until")
        public String contractUntil;
        /**
         * e.g. "available", "injured", "suspended"
         */
        public String availability;
        public PlayerAttributes attributes;
        public List<String> strengths;
        public List<String> weaknesses;
        public Integer rating;
        public String potential;
        @JsonProperty("injury_risk")
        public String injuryRisk;
        @JsonProperty("wage_eur_annual")
        public Long wageEurAnnual;

        @Override
        public String toString() {
            return String.format("#%-3s %-25s %-3s %-4s | rating %s | value €%,d | %s",
                    jerseyNumber != null ? jerseyNumber : "?",
                    name,
                    position != null ? position : "-",
                    specificPosition != null ? specificPosition : "-",
                    rating != null ? rating : "?",
                    marketValueEur != null ? marketValueEur : 0,
                    availability != null ? availability : "");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerAttributes {
        /**
         * Evaluated position (may differ from listed position)
         */
        public String position;
        public Integer tactical;
        public Integer attacking;
        public Integer defending;
        public Integer technical;
        public Integer creativity;
    }

    // ──────────────────────────────────────────────
    // Player career model classes
    // ──────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerCareer {
        @JsonProperty("player_id")
        public int playerId;
        public List<CareerSeason> seasons;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CareerSeason {
        @JsonProperty("season_id")
        public int seasonId;
        @JsonProperty("league_id")
        public int leagueId;
        @JsonProperty("team_id")
        public int teamId;
        public int matches;
        public int minutes;
        public int goals;
        public int assists;
        @JsonProperty("avg_rating")
        public double avgRating;

        @Override
        public String toString() {
            return String.format("season %-3d | team %-5d | matches %2d | mins %4d | goals %2d | assists %2d | avg rating %.2f",
                    seasonId, teamId, matches, minutes, goals, assists, avgRating);
        }
    }

    // ──────────────────────────────────────────────
    // Odds model classes
    // ──────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventOdds {
        @JsonProperty("event_id")
        public int eventId;
        public Odds odds;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Odds {
        @JsonProperty("home_win")
        public double homeWin;
        public double draw;
        @JsonProperty("away_win")
        public double awayWin;
        @JsonProperty("over_15_goals")
        public double over15Goals;
        @JsonProperty("over_25_goals")
        public double over25Goals;
        @JsonProperty("over_35_goals")
        public double over35Goals;
        @JsonProperty("under_15_goals")
        public double under15Goals;
        @JsonProperty("under_25_goals")
        public double under25Goals;
        @JsonProperty("under_35_goals")
        public double under35Goals;
        @JsonProperty("btts_yes")
        public double bttsYes;
        @JsonProperty("btts_no")
        public double bttsNo;

        @Override
        public String toString() {
            return String.format(
                    "  1X2       : home %.2f  draw %.2f  away %.2f%n" +
                            "  Over/Under: o1.5 %.2f  o2.5 %.2f  o3.5 %.2f%n" +
                            "              u1.5 %.2f  u2.5 %.2f  u3.5 %.2f%n" +
                            "  BTTS      : yes  %.2f  no   %.2f",
                    homeWin, draw, awayWin,
                    over15Goals, over25Goals, over35Goals,
                    under15Goals, under25Goals, under35Goals,
                    bttsYes, bttsNo);
        }
    }

    // ──────────────────────────────────────────────
    // Prediction model classes
    // ──────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PredictionsResponse {
        public int count;
        public String next;
        public String previous;
        public List<Prediction> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Prediction {
        public int id;
        @JsonProperty("created_at")
        public OffsetDateTime createdAt;
        public PredictionEvent event;
        public PredictionMarkets markets;
        public PredictionRecommendations recommendations;
        public PredictionModel model;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PredictionEvent {
        public int id;
        @JsonProperty("event_date")
        public OffsetDateTime eventDate;
        public String status;
        @JsonProperty("home_team_id")
        public int homeTeamId;
        @JsonProperty("home_team")
        public String homeTeam;
        @JsonProperty("away_team_id")
        public int awayTeamId;
        @JsonProperty("away_team")
        public String awayTeam;
        @JsonProperty("league_id")
        public int leagueId;
        @JsonProperty("league_name")
        public String leagueName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PredictionMarkets {
        @JsonProperty("match_result")
        public MatchResultMarket matchResult;
        @JsonProperty("expected_goals")
        public ExpectedGoalsMarket expectedGoals;
        @JsonProperty("over_under")
        public OverUnderMarket overUnder;
        public BttsMarket btts;
        public ScoreMarket score;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatchResultMarket {
        @JsonProperty("prob_home")
        public double probHome;
        @JsonProperty("prob_draw")
        public double probDraw;
        @JsonProperty("prob_away")
        public double probAway;
        /**
         * "H", "D", or "A"
         */
        public String predicted;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExpectedGoalsMarket {
        public double home;
        public double away;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OverUnderMarket {
        @JsonProperty("prob_over_15")
        public double probOver15;
        @JsonProperty("prob_over_25")
        public double probOver25;
        @JsonProperty("prob_over_35")
        public double probOver35;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BttsMarket {
        @JsonProperty("prob_yes")
        public double probYes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScoreMarket {
        @JsonProperty("most_likely")
        public String mostLikely;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PredictionRecommendations {
        public String favorite;
        @JsonProperty("favorite_prob")
        public double favoriteProb;
        @JsonProperty("bet_favorite")
        public boolean betFavorite;
        @JsonProperty("over_15")
        public boolean over15;
        @JsonProperty("over_25")
        public boolean over25;
        @JsonProperty("over_35")
        public boolean over35;
        public boolean btts;
        public boolean winner;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PredictionModel {
        public double confidence;
        public String version;
    }

    // ──────────────────────────────────────────────
    // Bookmaker odds model classes
    // ──────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OddsLineResponse {
        public int count;
        public String next;
        public String previous;
        public List<OddsLine> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OddsLine {
        public int id;
        @JsonProperty("event_id")
        public int eventId;
        /**
         * e.g. "1x2", "double_chance", "over_under_25", "btts", "draw_no_bet"
         */
        public String market;
        /**
         * e.g. "HOME", "DRAW", "AWAY", "over", "under", "yes", "no", "1X", "X2", "12"
         */
        public String outcome;
        /**
         * Line value for over/under markets (null for others)
         */
        public Double line;
        @JsonProperty("outcome_name")
        public String outcomeName;
        @JsonProperty("bookmaker_slug")
        public String bookmakerSlug;
        @JsonProperty("bookmaker_name")
        public String bookmakerName;
        @JsonProperty("decimal_odds")
        public double decimalOdds;
        @JsonProperty("previous_decimal_odds")
        public double previousDecimalOdds;
        @JsonProperty("implied_probability")
        public double impliedProbability;
        /**
         * "SHORTENING" (odds dropping, more likely) or "DRIFTING" (odds rising, less likely)
         */
        public String movement;
        @JsonProperty("is_max_quote")
        public boolean isMaxQuote;
        @JsonProperty("updated_at")
        public OffsetDateTime updatedAt;

        @Override
        public String toString() {
            return String.format("%-20s %-6s %-5s odds %5.3f (was %5.3f) prob %.2f%% %-10s %s",
                    market, outcome,
                    line != null ? line.toString() : "",
                    decimalOdds, previousDecimalOdds,
                    impliedProbability * 100,
                    movement,
                    isMaxQuote ? "[BEST]" : "");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EventData {
        public int event_id;
        public List<Incident> incidents;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Incident {
        public String text;
        public String type;
        public Integer minute;
        public Boolean is_live;

        public Integer away_score;
        public Integer home_score;

        // goal
        public String assist;
        public String player;
        public Boolean is_home;
        public String goal_type;
        public Integer player_id;
        public Integer added_time;

        // substitution
        public String player_in;
        public String player_out;
        public Integer player_in_id;
        public Integer player_out_id;

        // card
        public String card_type;

        // injury time
        public Integer length;

        @Override
        public String toString() {
            return "Incident{" +
                    "text='" + text + '\'' +
                    ", type='" + type + '\'' +
                    ", minute=" + minute +
                    ", is_live=" + is_live +
                    ", away_score=" + away_score +
                    ", home_score=" + home_score +
                    ", assist='" + assist + '\'' +
                    ", player='" + player + '\'' +
                    ", is_home=" + is_home +
                    ", goal_type='" + goal_type + '\'' +
                    ", player_id=" + player_id +
                    ", added_time=" + added_time +
                    ", player_in='" + player_in + '\'' +
                    ", player_out='" + player_out + '\'' +
                    ", player_in_id=" + player_in_id +
                    ", player_out_id=" + player_out_id +
                    ", card_type='" + card_type + '\'' +
                    ", length=" + length +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LineupResponse {
        public int event_id;
        public String lineup_status;
        public boolean beta;
        public Lineups lineups;
        public UnavailablePlayers unavailable_players;
        public String updated_at;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Lineups {
        public TeamLineup home;
        public TeamLineup away;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamLineup {
        public int team_id;
        public String team_name;
        public String formation;
        public Integer confidence;
        public List<Player> players;
        public List<Player> substitutes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnavailablePlayers {
        public List<Player> home;
        public List<Player> away;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamsResponse {
        public int count;
        public String next;
        public String previous;
        public List<Team> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        public int id;
        public String name;
        public String short_name;
        public String country;
        public Integer venue_id; // nullable
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Manager {
        public int id;
        public String name;
        public String short_name;
        public String country;
        public String tactical_profile;
        public String preferred_formation;
        public int current_team_id;
        public int matches_total;
        public int wins;
        public int draws;
        public int losses;
        public double win_pct;
        public double avg_goals_scored;
        public double avg_goals_conceded;
        public double avg_possession;
        public double clean_sheet_pct;
        public double btts_pct;
        public double over_25_pct;
        public String stats_updated_at;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StadiumResponse {

        public int count;
        public String next;
        public String previous;
        public List<Arena> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Arena {
        public int id;
        public String name;
        public String city;
        public String country;
        public String country_code;
        public int capacity;
        public double latitude;
        public double longitude;
        public int pitch_length_m;
        public int pitch_width_m;
        public int built_year;
        public int home_team_id;
    }

    // ──────────────────────────────────────────────
    // HTTP client
    // ──────────────────────────────────────────────

    /**
     * Fetch a single event by its ID.
     * Calls: GET /api/v2/events/{id}/
     */
    public Event fetchEvent(int eventId) throws Exception {
        String url = BASE_URL + eventId + "/";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = createHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), Event.class);
    }

    /**
     * Fetch a list of events filtered by league and date range.
     * Calls: GET /api/v2/events/?league_id=&date_from=&date_to=&status=
     */
    public EventsResponse fetchEvents(String dateFrom, String dateTo, int leagueId) throws Exception {
        return fetchEvents(dateFrom, dateTo, leagueId, "finished");
    }

    /**
     * Fetch a list of events filtered by league, date range, and status.
     * Calls: GET /api/v2/events/?league_id=&date_from=&date_to=&status=
     */
    public EventsResponse fetchEvents(String dateFrom, String dateTo, int leagueId, String status) throws Exception {
        String url = String.format("%s?league_id=%d&date_from=%s&date_to=%s&status=%s", BASE_URL, leagueId, dateFrom, dateTo, status);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = createHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), EventsResponse.class);
    }

    /**
     * Fetch a single incident  by its event ID.
     * Calls: GET /api/v2/events/{id}/incidents/
     */
    public EventData fetchIncidents(int eventId) throws Exception {
        String url = BASE_URL + eventId + "/incidents/";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = createHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), EventData.class);
    }

    /**
     * Fetch a single lineup by its event ID.
     * Calls: GET /api/v2/events/{id}/incidents/
     */
    public LineupResponse fetchLineups(int eventId) throws Exception {
        String url = BASE_URL + eventId + "/lineups/";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = createHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), LineupResponse.class);
    }

    /**
     * Fetch referees for a given league.
     * Calls: GET /api/v2/referees/?league_id={leagueId}
     */
    public RefereesResponse fetchReferees(int leagueId) throws Exception {
        String url = BASE_URL_REFEREES + "?league_id=" + leagueId;

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), RefereesResponse.class);
    }

    /**
     * Fetch players for a given team.
     * Calls: GET /api/v2/players/?team_id={teamId}
     */
    public PlayersResponse fetchPlayers(int teamId) throws Exception {
        String url = BASE_URL_PLAYERS + "?team_id=" + teamId;

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), PlayersResponse.class);
    }

    /**
     * Fetch career statistics for a single player.
     * Calls: GET /api/v2/players/{playerId}/career/
     */
    public PlayerCareer fetchPlayerCareer(int playerId) throws Exception {
        String url = BASE_URL_PLAYERS + playerId + "/career/";

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), PlayerCareer.class);
    }

    /**
     * Fetch a single manager
     * Calls: GET /api/v2/managers/{id}/
     */
    public Manager fetchManager(int managerId) throws Exception {
        String url = BASE_URL_MANAGERS + managerId + "/";

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), Manager.class);
    }

    /**
     * Fetch teams for a given league.
     * Calls: GET /api/v2/teams/?league_id={leagueId}
     */
    public TeamsResponse fetchTeams(int leagueId) throws Exception {
        String url = BASE_URL_TEAMS + "?league_id=" + leagueId;

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), TeamsResponse.class);
    }

    /**
     * Fetch odds for a single event.
     * Calls: GET /api/v2/events/{eventId}/odds/
     */
    public EventOdds fetchEventOdds(int eventId) throws Exception {
        String url = BASE_URL + eventId + "/odds/";

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), EventOdds.class);
    }

    /**
     * Fetch prediction for a single event.
     * Calls: GET /api/v2/events/{eventId}/prediction/
     */
    public Prediction fetchPrediction(int eventId) throws Exception {
        String url = BASE_URL + eventId + "/prediction/";

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), Prediction.class);
    }

    /**
     * Fetch predictions filtered by league and date range.
     * Calls: GET /api/v2/predictions/?league_id={leagueId}&date_from={dateFrom}&date_to={dateTo}
     */
    public PredictionsResponse fetchPredictions(int leagueId, String dateFrom, String dateTo) throws Exception {
        String url = String.format("%s?league_id=%d&date_from=%s&date_to=%s",
                BASE_URL_PREDICTIONS, leagueId, dateFrom, dateTo);

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), PredictionsResponse.class);
    }

    /**
     * Fetch bookmaker odds for a specific event, optionally filtered by bookmaker.
     * Calls: GET /api/v2/odds/?event_id={eventId}[&bookmaker_slug={slug}]
     * Handles pagination automatically, returning all pages merged.
     */
    public OddsLineResponse fetchOdds(int eventId, String bookmakerSlug) throws Exception {
        String url = BASE_URL_ODDS + "?event_id=" + eventId;
        if (bookmakerSlug != null && !bookmakerSlug.isEmpty()) {
            url += "&bookmaker_slug=" + bookmakerSlug;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        List<OddsLine> allResults = new java.util.ArrayList<>();
        int totalCount = 0;
        String nextUrl = url;

        while (nextUrl != null) {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(nextUrl))
                    .header("Authorization", "Token " + API_TOKEN)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
            }

            OddsLineResponse page = mapper.readValue(response.body(), OddsLineResponse.class);
            totalCount = page.count;
            allResults.addAll(page.results);
            nextUrl = page.next;
        }

        OddsLineResponse merged = new OddsLineResponse();
        merged.count = totalCount;
        merged.results = allResults;
        return merged;
    }

    /**
     * Fetch arena for a given team.
     * Calls: GET /api/v2/players/?team_id={teamId}
     */
    public StadiumResponse fetchStadium(int teamId) throws Exception {
        String url = BASE_URL_VENUES + "?team_id=" + teamId;

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + API_TOKEN)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API error " + response.statusCode() + ": " + response.body());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(response.body(), StadiumResponse.class);
    }

    private HttpClient createHttpClient() {
        HttpClient client = HttpClient.newBuilder()
                //.proxy(ProxySelector.of(new InetSocketAddress("proxy.sfa.se", 8080)))
                .connectTimeout(Duration.ofSeconds(1000))
                .build();
        // Uncomment the line below to disable SSL verification (equivalent to curl -k).
        // In production, use a proper trust store instead.
        // .sslContext(insecureSslContext())
        return client;
    }

    // ──────────────────────────────────────────────
    // Main – demo
    // ──────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        SportsApiClient client = new SportsApiClient();

        printArenas(client, 26);

        /*
        TeamsResponse teamsResponse = client.fetchTeams(26);
        int teamId = teamsResponse.results.getFirst().id;
        PlayersResponse playersResponse = client.fetchPlayers(teamId);
        Player player = playersResponse.results.getFirst();
        System.out.println(player.toString());
*/
        // ── Single incidents ──────────────────────────────
        // EventData eventData = client.fetchIncidents(46355);
        // System.out.println();

        // ── Lineups ──────────────────────────────
        /*
        LineupResponse lineupResponse = client.fetchLineups(46355);
        System.out.println();

        // ── Single event ──────────────────────────────
        Event single = client.fetchEvent(46355);
        System.out.println("Single event fetch: id=" + single.id);
        System.out.println(single);
        System.out.println();

        Manager manager = client.fetchManager(584);
        System.out.println();
        */

        // ── Event list ────────────────────────────────
        EventsResponse response = client.fetchEvents("2026-04-04", "2026-04-17", 26);
        System.out.printf("Total events: %d%n%n", response.count);

        for (Event event : response.results) {
            System.out.println(event.id);
            System.out.println(event);

            if (event.weather != null) {
                System.out.printf("  Weather: %s, %.1f°C, wind %.1f km/h%n",
                        event.weather.description,
                        event.weather.temperatureC,
                        event.weather.windSpeed);
            }

            HeadToHead h2h = event.headToHead;
            if (h2h != null) {
                System.out.printf("  H2H (%d matches): home wins %d, draws %d, away wins %d%n",
                        h2h.totalMatches, h2h.homeWins, h2h.draws, h2h.awayWins);
            }

            if (event.highlights != null && !event.highlights.isEmpty()) {
                System.out.printf("  Highlight: %s%n", event.highlights.get(0).url);
            }

            System.out.println();
        }

      /*

        // ── Referees ──────────────────────────────────
        /*System.out.println("\n=== Referees (league 26) ===");
        RefereesResponse refs = client.fetchReferees(26);
        System.out.printf("Total referees: %d%n%n", refs.count);
        for (Referee ref : refs.results) {
            System.out.println(ref);
        }*/

        /*
        // ── Players ───────────────────────────────────
        System.out.println("\n=== Players (team 439 – IFK Göteborg) ===");
        PlayersResponse players = client.fetchPlayers(439);
        System.out.printf("Total players: %d%n%n", players.count);
        for (Player player : players.results) {
            System.out.println(player);
            if (player.attributes != null) {
                System.out.printf("   attrs → tac:%d atk:%d def:%d tec:%d cre:%d%n",
                        player.attributes.tactical,
                        player.attributes.attacking,
                        player.attributes.defending,
                        player.attributes.technical,
                        player.attributes.creativity);
            }
            if (player.strengths != null && !player.strengths.isEmpty()) {
                System.out.println("   strengths: " + player.strengths);
            }
            if (player.weaknesses != null && !player.weaknesses.isEmpty()) {
                System.out.println("   weaknesses: " + player.weaknesses);
            }
        }

        // ── Player Career ─────────────────────────────
        System.out.println("\n=== Player Career (player 15907) ===");
        PlayerCareer career = client.fetchPlayerCareer(15907);
        System.out.printf("Player ID: %d | seasons: %d%n%n", career.playerId, career.seasons.size());
        for (CareerSeason season : career.seasons) {
            System.out.println(season);
        }
*/

        // ── Event Odds ────────────────────────────────
        System.out.println("\n=== Event Odds (event 46355) ===");
        EventOdds eventOdds = client.fetchEventOdds(46355);
        System.out.printf("Event ID: %d%n", eventOdds.eventId);
        System.out.println(eventOdds.odds);


        // ── Bookmaker Odds Lines ───────────────────────
        System.out.println("\n=== Bookmaker Odds (event 46324, Pinnacle) ===");
        OddsLineResponse oddsLines = client.fetchOdds(46324, "pinnacle");
        System.out.printf("Total odds lines: %d%n%n", oddsLines.count);
        String currentMarket = null;
        for (OddsLine ol : oddsLines.results) {
            if (!ol.market.equals(currentMarket)) {
                currentMarket = ol.market;
                System.out.println("  -- " + currentMarket + " --");
            }
            System.out.println("  " + ol);
        }


        Prediction prediction = client.fetchPrediction(46388);
        System.out.println("Prediction: " + prediction);
/*
        // ── Predictions ───────────────────────────────
        System.out.println("\n=== Predictions (league 27, 2026-06-11 to 2026-06-12) ===");
        PredictionsResponse preds = client.fetchPredictions(27, "2026-06-11", "2026-06-19");
        System.out.printf("Total predictions: %d%n%n", preds.count);
        for (Prediction pred : preds.results) {
            PredictionEvent ev = pred.event;
            System.out.printf("[%s] %s vs %s (%s)%n",
                    ev.leagueName, ev.homeTeam, ev.awayTeam, ev.eventDate);

            MatchResultMarket mr = pred.markets.matchResult;
            System.out.printf("  1X2 probs   : H %.1f%%  D %.1f%%  A %.1f%%  → predicted: %s%n",
                    mr.probHome, mr.probDraw, mr.probAway, mr.predicted);

            ExpectedGoalsMarket xg = pred.markets.expectedGoals;
            System.out.printf("  xG          : home %.2f  away %.2f%n", xg.home, xg.away);

            OverUnderMarket ou = pred.markets.overUnder;
            System.out.printf("  Over probs  : o1.5 %.1f%%  o2.5 %.1f%%  o3.5 %.1f%%%n",
                    ou.probOver15, ou.probOver25, ou.probOver35);

            System.out.printf("  BTTS yes    : %.1f%%%n", pred.markets.btts.probYes);
            System.out.printf("  Most likely : %s%n", pred.markets.score.mostLikely);

            PredictionRecommendations rec = pred.recommendations;
            System.out.printf("  Bets flagged: favorite=%s  o1.5=%s  o2.5=%s  btts=%s  winner=%s%n",
                    rec.betFavorite, rec.over15, rec.over25, rec.btts, rec.winner);

            System.out.printf("  Model       : confidence %.3f  version %s%n",
                    pred.model.confidence, pred.model.version);
            System.out.println();
        }

 */
    }


    static void printArenas(SportsApiClient client, int leagueId) throws Exception {
        TeamsResponse teamsResponse = client.fetchTeams(leagueId);
        for (Team team : teamsResponse.results) {
            StadiumResponse stadiumResponse = client.fetchStadium(team.id);
            if (stadiumResponse.results != null && !stadiumResponse.results.isEmpty()) {
                Arena arena = stadiumResponse.results.getFirst();
                System.out.println("arenas.put(" + arena.id + ", \"" + arena.name + " (" + arena.city + ") Kapacitet: " + arena.capacity + "\");");
            }
        }

    }

}
