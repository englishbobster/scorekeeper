package stos.keeper.sparkServer;

import stos.keeper.database.DbPlannedMatchesDAO;
import stos.keeper.database.PostgresDataSourceImpl;
import stos.keeper.sparkServer.json.PlannedMatchesResponseTransformer;

import static spark.Spark.*;

public class Bootstrap {

    public static void main(String[] args) {
        DbPlannedMatchesDAO plannedMatchesDAO = new DbPlannedMatchesDAO(new PostgresDataSourceImpl());
        staticFiles.externalLocation(System.getProperty("user.dir") + "/server/src/main/resources/public");

        PlannedMatchesResponseTransformer transformer = new PlannedMatchesResponseTransformer();
        get("/plannedmatches", (req, response) -> plannedMatchesDAO.getAllPlannedMatches(), transformer);
        after((req, response) -> response.type("application/json"));
    }
}
