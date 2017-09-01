import org.eclipse.jetty.server.Response;
import stos.keeper.database.PlannedMatchesDAO;
import stos.keeper.database.PlayerDAO;
import stos.keeper.database.PostgresDataSourceImpl;
import stos.keeper.model.planned_matches.Score;
import stos.keeper.model.player.Player;
import stos.keeper.sparkServer.json.JsonTransformer;

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

        get("/plannedmatches", (request, response) -> plannedMatchesDAO.getAllPlannedMatches(), transformer);

        put("/plannedmatches/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Score score = transformer.scoreFromJson(request.body());
            return plannedMatchesDAO.setFinalScoreForMatchById(id, score);
        });

        get("/player/:name", (request, response) -> {
            Optional<Player> playerByName = playerDAO.getPlayerByName(request.params(":name"));
            if (playerByName.isPresent()) {
                return playerByName.get();
            } else {
                response.status(Response.SC_NOT_FOUND);
                return "Player not found.";
            }
        }, transformer);

        post("/player", (request, response) -> {
            Player player = transformer.playerFromJson(request.body());
            return playerDAO.addPlayer(player);
        }, transformer);
    }
}

