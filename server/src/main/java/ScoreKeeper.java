import org.eclipse.jetty.server.Response;
import stos.keeper.database.PlannedMatchesDAO;
import stos.keeper.database.PlayerDAO;
import stos.keeper.database.PostgresDataSourceImpl;
import stos.keeper.model.planned_matches.FootballMatch;
import stos.keeper.model.planned_matches.Score;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;
import stos.keeper.sparkServer.Routes.RegisterPlayerRoute;

import java.util.List;
import java.util.Optional;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

public class ScoreKeeper {

    public static void main(String[] args) {
        PostgresDataSourceImpl dataSource = new PostgresDataSourceImpl();
        PlannedMatchesDAO plannedMatchesDAO = new PlannedMatchesDAO(dataSource);
        PlayerDAO playerDAO = new PlayerDAO(dataSource);
        JsonTransformer transformer = new JsonTransformer();
        port(5000);
        staticFiles.externalLocation(System.getProperty("user.dir") + "/server/src/main/resources/public");

        after((request, response) -> response.type("application/json"));

        get("/plannedmatches", (request, response) -> {
            List<FootballMatch> allPlannedMatches = plannedMatchesDAO.getAllPlannedMatches();
            response.status(Response.SC_OK);
            return allPlannedMatches;
        }, transformer);

        put("/plannedmatches/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Score score = transformer.scoreFromJson(request.body());
            int result = plannedMatchesDAO.setFinalScoreForMatchById(id, score);
            if(result == 0){
                response.status(Response.SC_NOT_FOUND);
                return "Match not found.";
            }else{
                response.status(Response.SC_OK);
                return "Match updated.";
            }
        });

        post(RegisterPlayerRoute.PATH, new RegisterPlayerRoute(transformer, playerDAO), transformer);

        get("/player/:name", (request, response) -> {
            Optional<Player> playerByName = playerDAO.getPlayerByName(request.params(":name"));
            if (playerByName.isPresent()) {
                response.status(Response.SC_OK);
                return playerByName.get();
            } else {
                response.status(Response.SC_NOT_FOUND);
                return "Player not found.";
            }
        }, transformer);

    }
}

