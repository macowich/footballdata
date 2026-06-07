package se.mac.footballdata.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import se.mac.footballdata.rest.model.Result;

import java.util.List;

@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResultResource
{
   @Inject
   ResultRepository resultRepository;

   @GET
   public List<Result> results()
   {
      return Result.listAll();
   }

   @GET
   @Path("/team/{team}")
   public List<Result> team(@PathParam("team") String team)
   {
      return resultRepository.findByTeam(team);
   }

   @GET
   @Path("/league/{league}")
   public List<Result> league(@PathParam("league") String league)
   {
      return resultRepository.findByLeague(league);
   }

   // Hämta en specifik via ID
   @GET
   @Path("/{id}")
   public Result getById(@PathParam("id") String id)
   {
      Result result = Result.findById(new ObjectId(id));
      if (result == null)
      {
         throw new WebApplicationException("Hittades inte", Response.Status.NOT_FOUND);
      }
      return result;
   }
}
