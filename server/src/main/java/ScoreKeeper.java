import stos.keeper.database.DbPlannedMatchesDAO;
import stos.keeper.database.PostgresDataSourceImpl;
import stos.keeper.model.Score;
import stos.keeper.sparkServer.json.PlannedMatchesResponseTransformer;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

public class ScoreKeeper {

    public static void main(String[] args) {
        DbPlannedMatchesDAO plannedMatchesDAO = new DbPlannedMatchesDAO(new PostgresDataSourceImpl());
        PlannedMatchesResponseTransformer transformer = new PlannedMatchesResponseTransformer();
        port(5000);
        staticFiles.externalLocation(System.getProperty("user.dir") + "/server/src/main/resources/public");

        after((req, response) -> response.type("application/json"));

        get("/plannedmatches", (req, response) -> plannedMatchesDAO.getAllPlannedMatches(), transformer);

        put("/plannedmatches/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Score score = transformer.scoreFromJson(request.body());
            return plannedMatchesDAO.setFinalScoreForMatchById(id, score);
        });
    }
}
