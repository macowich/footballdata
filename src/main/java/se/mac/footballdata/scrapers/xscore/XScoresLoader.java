package se.mac.footballdata.scrapers.xscore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.List;

public class XScoresLoader {

    private static final String BASE_PATH = "C:\\data\\xscore\\";
    public static List<String> resultPages = new ArrayList<>();

    static {
        resultPages.add(BASE_PATH + "results\\Superettan - Results _ Football Sweden.html");
        resultPages.add(BASE_PATH + "results\\Ettan - Results _ Football Sweden.html");
        resultPages.add(BASE_PATH + "results\\Ettan Sodra - Results _ Football Sweden.html");
    }

    public static void main(String[] args) throws Exception {
        for (String page : resultPages) {
            int leagueId = 1000;
            if (page.contains("Ettan Sodra")) {
                leagueId = 1002;
            } else if (page.contains("Ettan - Results")) {
                leagueId = 1001;
            }

            List<String> urlList = loadResultpage(page);
            for (String url : urlList) {
                fetchDataFiles(url, leagueId);
            }
        }
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

    private static void fetchDataFiles(String url, int leagueId) throws Exception {
        System.out.println("-> Reading data from server " + url);
        String name = url.substring(url.lastIndexOf("/") + 1);
        name = name + ".json";
        String outFile = BASE_PATH + "json" + File.separator + leagueId + File.separator + name;
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
