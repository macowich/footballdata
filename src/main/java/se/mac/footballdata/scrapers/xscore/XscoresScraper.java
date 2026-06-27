package se.mac.footballdata.scrapers.xscore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.mac.footballdata.DBUtil;
import se.mac.footballdata.scrapers.xscore.model.Incident;
import se.mac.footballdata.scrapers.xscore.model.MatchData;
import se.mac.footballdata.scrapers.xscore.model.OutcomeWrapper;
import se.mac.footballdata.sportsapi.db.EventDB;
import se.mac.footballdata.sportsapi.db.IncidentDB;
import se.mac.footballdata.sportsapi.db.OddsDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

//
//https://api.xscores.com/v1/json/stages/61566/events?language-type=3&round-name=12&timezone=Europe/Stockholm"

public class XscoresScraper {

    private final static String DB_CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String BASE_PATH = "C:\\data\\xscore\\";
    //private static final String BASE_PATH_FK = "C:\\FKApps\\data\\xscore\\";

    public static void main(String[] args) throws Exception {
        /*
        List<String> urlList = loadResultpage(BASE_PATH + "results\\Superettan - Results _ Football Sweden.html");
        for (String url : urlList) {
            fetchDataFiles(url);
        }*/

        int leagueId = 1000; // Superettan
        List<MatchData> matchDataList = loadAllMatchData();
        List<EventDB> eventDBList = matchDataList.stream()
                .map(match -> createEventDB(match, leagueId))
                .toList();
        ArrayList<IncidentDB> incidentDBList = new ArrayList<>();
        for (MatchData matchData: matchDataList) {
            List<IncidentDB> incidentList = createIncidentDBList(matchData);
            incidentDBList.addAll(incidentList);
        }

        List<OddsDB> oddsDBList = matchDataList.stream()
                .map(XscoresScraper::createOddsDB)
                .filter(Objects::nonNull)
                .toList();

        updateDatabase(eventDBList, oddsDBList, incidentDBList);
    }

    private static List<IncidentDB> createIncidentDBList(MatchData matchData) {
        ArrayList<IncidentDB> incidentDBList = new ArrayList<>();

        for (Incident i : matchData.incidents) {
            IncidentDB incidentDB = new IncidentDB();
            incidentDB.eventId = Math.toIntExact(matchData.id);
            incidentDB.type = i.typeName != null ? getType(i.typeName) : "";
            incidentDB.text = i.reason;
            incidentDB.home = i.side.equalsIgnoreCase("home");
            incidentDB.player = i.playerName;
            incidentDB.playerId = i.playerId != null ? i.playerId : 0;
            incidentDB.cardType = i.typeName != null ? getCardType(i.typeName) : "";
            incidentDB.goalType = incidentDB.type.equalsIgnoreCase("goal") ? "regular" : "";
            incidentDB.assist = null;
            incidentDB.minute = i.elapsed;
            incidentDB.playerIn = "unknown";
            incidentDB.playerOut = incidentDB.type.equalsIgnoreCase("substitution") ? i.playerName : "";
            incidentDBList.add(incidentDB);
        }
        return incidentDBList;
    }

    private static String getType(String typeName) {
        if (typeName.equalsIgnoreCase("Yellow card")) return "card";
        else if (typeName.equalsIgnoreCase("Red card")) return "card";
        else if (typeName.equalsIgnoreCase("Substitution out")) return "substitution";
        else if (typeName.equalsIgnoreCase("Regular goal")) return "goal";

        return "";
    }

    private static String getCardType(String typeName) {
        if (typeName.equalsIgnoreCase("Yellow card")) return "yellow";
        else if (typeName.equalsIgnoreCase("Red card")) return "red";
        return "";
    }

    private static List<MatchData> loadAllMatchData() throws IOException {
        List<MatchData> matchDataList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                Paths.get(BASE_PATH + File.separator + "json"), "*.json")) {

            for (Path entry : stream) {
                MatchData matchData = loadMatchData(String.valueOf(entry.getFileName()));
                matchDataList.add(matchData);
            }
        }
        return matchDataList;
    }

    private static void updateDatabase(List<EventDB> eventDBList, List<OddsDB> oddsDBList, List<IncidentDB> incidentDBList) {
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

            DBUtil.updateEventsCollection(database, eventDBList);
            DBUtil.updateIncidentsCollection(database, incidentDBList);
            DBUtil.updateOddsCollection(database, oddsDBList);
        }
    }

    private static EventDB createEventDB(MatchData matchData, int leagueId) {
        EventDB eventDB = new EventDB();
        eventDB.leagueId = leagueId;
        eventDB.round = Integer.parseInt(matchData.roundName);
        eventDB.date = matchData.start.substring(0, 10);
        eventDB.time = matchData.start.substring(11, matchData.start.length() - 3);
        eventDB.eventId = Math.toIntExact(matchData.id);
        eventDB.hometeam = matchData.home.getFirst().name;
        eventDB.awayteam = matchData.away.getFirst().name;
        int[] score = parseScore(matchData.results.getFirst().value);
        eventDB.homeScore = score[0];
        eventDB.awayScore = score[1];
        score = parseScore(matchData.results.get(2).value);
        eventDB.homeScoreHt = score[0];
        eventDB.awayScoreHt = score[1];

        parseIncidents(eventDB, matchData.incidents);

        return eventDB;
    }

    private static void parseIncidents(EventDB eventDB, List<Incident> incidents) {
        int yCardsHome = 0;
        int rCardsHome = 0;
        int yCardsAway = 0;
        int rCardsAway = 0;
        for (Incident incident : incidents) {
            if (incident.typeName.equalsIgnoreCase("Yellow card")) {
                if (incident.side.equalsIgnoreCase("home")) {
                    yCardsHome++;
                } else {
                    yCardsAway++;
                }
            } else if (incident.typeName.equalsIgnoreCase("Red card")) {
                if (incident.side.equalsIgnoreCase("home")) {
                    rCardsHome++;
                } else {
                    rCardsAway++;
                }
            }
        }
        eventDB.hy = yCardsHome;
        eventDB.ay = yCardsAway;
        eventDB.hr = rCardsHome;
        eventDB.ar = rCardsAway;
    }

    private static int[] parseScore(String value) {
        int[] score = new int[2];
        score[0] = Integer.parseInt(value.substring(0, 1));
        score[1] = Integer.parseInt(value.substring(2));
        return score;
    }

    private static OddsDB createOddsDB(MatchData matchData) {
        if (matchData.outcomes == null || matchData.outcomes.isEmpty()) return null;

        OddsDB oddsDB = new OddsDB();
        oddsDB.eventId = Math.toIntExact(matchData.id);
        for (OutcomeWrapper outcomeWrapper : matchData.outcomes) {
            if (outcomeWrapper.providerName.equalsIgnoreCase("Bet365")) {
                oddsDB.homeWin = outcomeWrapper.outcomes.getFirst().odds;
                oddsDB.draw = outcomeWrapper.outcomes.get(1).odds;
                oddsDB.awayWin = outcomeWrapper.outcomes.get(2).odds;
            }
        }
        return oddsDB;
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
            System.out.println("File " + name + " already exists");
            return;
        }
        Thread.sleep(2 * 1000);
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
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(1000))
                .build();
    }
}
