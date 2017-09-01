import stos.keeper.database.PlannedMatchesDAO;
import stos.keeper.database.PlayerDAO;
import stos.keeper.database.PostgresDataSourceImpl;
import stos.keeper.model.planned_matches.Score;
import stos.keeper.sparkServer.json.JsonTransformer;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
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

        get("/user/:name", (request, response) -> playerDAO.getUserByName(request.params(":name")),transformer);
    }
}
