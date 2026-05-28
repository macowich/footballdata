package se.mac.footballdata.rest;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ResultRepository implements PanacheMongoRepository<Result>
{

   public List<Result> findByTeam(String team)
   {
      List<Result> resultList = find(
            "{$or:[{hometeam:?1},{awayteam:?1}]}",
            team
      ).list();
      return resultList;
   }

   public List<Result> findByLeague(String league)
   {
      return list("league", league);
   }
}
