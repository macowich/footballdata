package se.mac.footballdata.sportsapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.List;

public class SportsApiClient {

    private static final String BASE_URL = "https://sports.bzzoiro.com/api/v2/events/";
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
        @JsonProperty("league_id")   public int leagueId;
        @JsonProperty("season_id")   public int seasonId;
        @JsonProperty("home_team_id") public int homeTeamId;
        @JsonProperty("home_team")   public String homeTeam;
        @JsonProperty("away_team_id") public int awayTeamId;
        @JsonProperty("away_team")   public String awayTeam;
        @JsonProperty("home_coach_id") public Integer homeCoachId;
        @JsonProperty("away_coach_id") public Integer awayCoachId;
        @JsonProperty("referee_id")  public Integer refereeId;
        @JsonProperty("venue_id")    public Integer venueId;
        @JsonProperty("event_date")  public OffsetDateTime eventDate;
        public String status;
        @JsonProperty("replaced_by") public Integer replacedBy;
        @JsonProperty("round_number") public int roundNumber;
        @JsonProperty("round_name")  public String roundName;
        @JsonProperty("group_name")  public String groupName;
        public String period;
        @JsonProperty("current_minute") public int currentMinute;
        @JsonProperty("home_score")  public int homeScore;
        @JsonProperty("away_score")  public int awayScore;
        @JsonProperty("home_score_ht") public int homeScoreHt;
        @JsonProperty("away_score_ht") public int awayScoreHt;
        @JsonProperty("penalty_shootout") public Object penaltyShootout;
        @JsonProperty("extra_time_score") public Object extraTimeScore;
        @JsonProperty("is_local_derby") public boolean isLocalDerby;
        @JsonProperty("is_neutral_ground") public boolean isNeutralGround;
        @JsonProperty("travel_distance_km") public double travelDistanceKm;
        public Weather weather;
        @JsonProperty("pitch_condition") public int pitchCondition;
        public Integer attendance;
        @JsonProperty("live_websocket") public boolean liveWebsocket;
        public List<Highlight> highlights;
        @JsonProperty("head_to_head") public HeadToHead headToHead;

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
        @JsonProperty("wind_speed") public double windSpeed;
        @JsonProperty("temperature_c") public double temperatureC;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Highlight {
        public String kind;
        public String title;
        public String url;
        public String thumbnail;
        @JsonProperty("published_at") public OffsetDateTime publishedAt;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HeadToHead {
        @JsonProperty("total_matches")  public int totalMatches;
        @JsonProperty("home_wins")      public int homeWins;
        public int draws;
        @JsonProperty("away_wins")      public int awayWins;
        @JsonProperty("home_goals")     public int homeGoals;
        @JsonProperty("away_goals")     public int awayGoals;
        @JsonProperty("avg_total_goals") public double avgTotalGoals;
        @JsonProperty("home_win_rate")  public double homeWinRate;
        @JsonProperty("away_win_rate")  public double awayWinRate;
        @JsonProperty("recent_matches") public List<RecentMatch> recentMatches;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecentMatch {
        public String home;
        public String away;
        public String score;
        public OffsetDateTime date;
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
        return mapper.readValue(response.body(), Event.class);
    }

    /**
     * Fetch a list of events filtered by league and date range.
     * Calls: GET /api/v2/events/?league_id=&date_from=&date_to=
     */
    public EventsResponse fetchEvents(String dateFrom, String dateTo, int leagueId) throws Exception {
        String url = String.format("%s?league_id=%d&date_from=%s&date_to=%s",
                BASE_URL, leagueId, dateFrom, dateTo);

        HttpClient client = HttpClient.newBuilder()
                // Uncomment the line below to disable SSL verification (equivalent to curl -k).
                // In production, use a proper trust store instead.
                // .sslContext(insecureSslContext())
                .build();

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
        return mapper.readValue(response.body(), EventsResponse.class);
    }

    // ──────────────────────────────────────────────
    // Main – demo
    // ──────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        SportsApiClient client = new SportsApiClient();

        // ── Single event ──────────────────────────────
        Event single = client.fetchEvent(46375);
        System.out.println("Single event fetch:");
        System.out.println(single);
        System.out.println();

        // ── Event list ────────────────────────────────
        EventsResponse response = client.fetchEvents("2026-05-24", "2026-05-25", 26);
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
    }
}
