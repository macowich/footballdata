package se.mac.footballdata.scrapers.xscore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.mac.footballdata.scrapers.xscore.model.MatchData;
import se.mac.footballdata.sportsapi.db.EventDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

//
//https://api.xscores.com/v1/json/stages/61566/events?language-type=3&round-name=12&timezone=Europe/Stockholm"

public class XscoresScraper {

    private final static String DB_CONNECTION_STRING = "mongodb://localhost:27017";
    private static String BASE_PATH = "C:\\data\\xscore\\";

    public static void main(String[] args) throws Exception {
        List<String> urlList = loadResultpage(BASE_PATH + "results\\Superettan - Results _ Football Sweden.html");
        /*
        fetchDataFiles(urlList.getFirst());
        for (String url : urlList) {
            fetchDataFiles(url);

            Thread.sleep(2 * 1000);
        }*/

        MatchData matchData = loadMatchData("2620299.json");
        System.out.println("Winner: " + matchData.winner);
        System.out.println("Home team: " + matchData.home.getFirst().name);

        EventDB eventDB = createEventDB(matchData);

        // Enable POJO codec support
        CodecProvider pojoCodecProvider =
                PojoCodecProvider.builder().automatic(true).build();

        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(pojoCodecProvider)
                );

        try (MongoClient mongoClient = MongoClients.create(DB_CONNECTION_STRING)) {

            MongoDatabase database = mongoClient
                    .getDatabase("sportsdb")
                    .withCodecRegistry(pojoCodecRegistry);

            MongoCollection<EventDB> collection =
                    database.getCollection("events", EventDB.class);

            try {
                collection.insertMany(Collections.singletonList(eventDB), new InsertManyOptions().ordered(false));
            } catch (MongoException ex) {
                System.out.println("Error: " + ex);
            }

            // Read back
            EventDB result = collection.find().first();
            assert result != null;
            System.out.println("Read from MongoDB: " + result.eventId);
        }
    }

    private static EventDB createEventDB(MatchData matchData) {
        EventDB eventDB = new EventDB();
        eventDB.leagueId = 26;
        eventDB.round = Integer.parseInt(matchData.roundName);
        eventDB.date = matchData.stageStart.substring(0, 10);
        eventDB.time = matchData.stageStart.substring(11);
        eventDB.eventId = Math.toIntExact(matchData.id);
        eventDB.hometeam = matchData.home.getFirst().name;
        eventDB.awayteam = matchData.away.getFirst().name;
        int[] score = parseScore(matchData.results.getFirst().value);
        eventDB.homeScore = score[0];
        eventDB.awayScore = score[1];
        score = parseScore(matchData.results.get(2).value);
        eventDB.homeScoreHt = score[0];
        eventDB.awayScoreHt = score[1];

        return eventDB;
    }

    private static int[] parseScore(String value) {
        int[] score = new int[2];
        score[0] = Integer.parseInt(value.substring(0, 1));
        score[1] = Integer.parseInt(value.substring(2));
        return score;
    }


    static MatchData loadMatchData(String filename) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(BASE_PATH + File.separator + "json" + File.separator + filename)));
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, MatchData.class);
    }

    static List<String> loadResultpage(String filename) throws Exception {
        List<String> urlList = new ArrayList<>();

        Document doc = Jsoup.parse(new File(filename), "UTF-8");
        // 1. Select ALL matching anchor tags into a collection
        Elements matchLinks = doc.select("a.ind_match_wrapper");

        System.out.println("Total matches found: " + matchLinks.size());
        System.out.println("-------------------------------------");

        // 2. Loop through the collection
        for (Element link : matchLinks) {
            // Extract the URL string
            String url = link.attr("href");
            System.out.println("Link: " + url);
            System.out.println();
            urlList.add(url);
        }

        return urlList;
    }

    private static void fetchDataFiles(String url) throws Exception {
        System.out.println("-> Reading data from server " + url);
        String name = url.substring(url.lastIndexOf("/"));
        name = name + ".json";
        String outFile = BASE_PATH + "json" + File.separator + name;
        if (new File(outFile).exists()) {
            System.out.println("File " + name + " + already exists");
            return;
        }
        System.out.println("--> Saving into file " + outFile);
        saveMatchFile(url, outFile);
    }

    static void saveMatchFile(String urlStr, String outFile) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("Accept", "application/html")
                .GET()
                .build();

        String jsonContent = "";

        HttpResponse<String> response = createHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        try (BufferedReader reader = new BufferedReader(new StringReader(response.body()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("let matchData")) {
                    System.out.println(line);
                    line = line.trim();
                    jsonContent = line.trim().substring(16, line.length() - 1);
                    break;
                }
            }
        }

        Path path = Paths.get(outFile);
        Files.writeString(path, jsonContent);
    }

    private static HttpClient createHttpClient() {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(1000))
                .build();
        return client;
    }
}
