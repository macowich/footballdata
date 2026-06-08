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
     * Calls: GET /api/v2/events/?league_id=&date_from=&date_to=
     */
    public EventsResponse fetchEvents(String dateFrom, String dateTo, int leagueId) throws Exception {
        String url = String.format("%s?league_id=%d&date_from=%s&date_to=%s", BASE_URL, leagueId, dateFrom, dateTo);
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

        // ── Single event ──────────────────────────────
        /*Event single = client.fetchEvent(46375);
        System.out.println("Single event fetch:");
        System.out.println(single);
        System.out.println();*/

        // ── Event list ────────────────────────────────
       /* EventsResponse response = client.fetchEvents("2026-04-04", "2026-05-25", 1);
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

        */

        // ── Referees ──────────────────────────────────
        /*System.out.println("\n=== Referees (league 26) ===");
        RefereesResponse refs = client.fetchReferees(26);
        System.out.printf("Total referees: %d%n%n", refs.count);
        for (Referee ref : refs.results) {
            System.out.println(ref);
        }*/

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

    }
}
