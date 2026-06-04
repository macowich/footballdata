package se.mac.footballdata.sportsapi.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import se.mac.footballdata.sportsapi.stats.EventStats;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Client for fetching match event statistics from the sports API.
 *
 * <pre>
 * Example usage:
 *   EventStatsClient client = new EventStatsClient("YOUR_TOKEN");
 *   EventStats stats = client.fetchEventStats(365);
 * </pre>
 */
public class EventStatsClient {

    private static final String BASE_URL = "https://sports.bzzoiro.com/api/v2";

    private final String token;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public EventStatsClient(String token) {
        this.token = token;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                // Disable SSL verification for self-signed certs (matches curl -k)
                .sslContext(createTrustAllSslContext())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Fetches stats for a given event ID.
     *
     * @param eventId the event/match ID
     * @return parsed {@link EventStats}
     * @throws IOException          on JSON parse errors
     * @throws InterruptedException if the request is interrupted
     */
    public EventStats fetchEventStats(int eventId) throws IOException, InterruptedException {
        String url = BASE_URL + "/events/" + eventId + "/stats/";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Token " + token)
                .header("Accept", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API returned HTTP " + response.statusCode()
                    + " for event " + eventId + ": " + response.body());
        }

        return objectMapper.readValue(response.body(), EventStats.class);
    }

    /**
     * Creates an SSL context that trusts all certificates (equivalent to curl -k).
     * Only use in development or when the server uses a self-signed certificate.
     */
    private static javax.net.ssl.SSLContext createTrustAllSslContext() {
        try {
            javax.net.ssl.TrustManager[] trustAll = new javax.net.ssl.TrustManager[]{
                new javax.net.ssl.X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] c, String a) {}
                    public void checkServerTrusted(java.security.cert.X509Certificate[] c, String a) {}
                }
            };
            javax.net.ssl.SSLContext ctx = javax.net.ssl.SSLContext.getInstance("TLS");
            ctx.init(null, trustAll, new java.security.SecureRandom());
            return ctx;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create trust-all SSL context", e);
        }
    }
}
