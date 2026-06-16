package se.mac.footballdata.scrapers.xscore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.mac.footballdata.scrapers.xscore.model.Match;
import se.mac.footballdata.scrapers.xscore.model.MatchData;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LoadMatchJson
{
   private static final String BASE_PATH = "C:\\data\\xscore\\json\\";

   public static void main(String[] args) throws Exception
   {
      String json = new String(Files.readAllBytes(Paths.get(BASE_PATH + "2620299.json")));
      ObjectMapper mapper = new ObjectMapper();

      MatchData data = mapper.readValue(json, MatchData.class);

      System.out.println("Winner: " + data.winner);
      System.out.println("Home team: " + data.home.getFirst().name);
   }
}

/*
json = new String(Files.readAllBytes(Paths.get(BASE_PATH + "xscore_supertettan.json")));
mapper = new ObjectMapper();

List<Match> matches = mapper.readValue(json, new TypeReference<List<Match>>() {});


 */