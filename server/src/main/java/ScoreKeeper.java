import stos.keeper.database.DbPlannedMatchesDAO;
import stos.keeper.database.PostgresDataSourceImpl;
import stos.keeper.sparkServer.json.PlannedMatchesResponseTransformer;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.staticFiles;

public class ScoreKeeper {

    public static void main(String[] args) {
        DbPlannedMatchesDAO plannedMatchesDAO = new DbPlannedMatchesDAO(new PostgresDataSourceImpl());
        staticFiles.externalLocation(System.getProperty("user.dir") + "/server/src/main/resources/public");

        PlannedMatchesResponseTransformer transformer = new PlannedMatchesResponseTransformer();
        get("/plannedmatches", (req, response) -> plannedMatchesDAO.getAllPlannedMatches(), transformer);
        after((req, response) -> response.type("application/json"));
    }
}
