import com.google.gson.Gson;
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
        port(5000);
        staticFiles.externalLocation(System.getProperty("user.dir") + "/server/src/main/resources/public");

        PlannedMatchesResponseTransformer transformer = new PlannedMatchesResponseTransformer();
        get("/plannedmatches", (req, response) -> plannedMatchesDAO.getAllPlannedMatches(), transformer);
        after((req, response) -> response.type("application/json"));

        put("/plannedmatches/:id", (request, response) -> {
            int id = Integer.parseInt(request.params(":id"));
            Score score = new Gson().fromJson(request.body(), Score.class);
            return plannedMatchesDAO.setFinalScoreForMatchById(id, score);
        });


    }

}
