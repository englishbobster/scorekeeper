import org.eclipse.jetty.server.Response;
import stos.keeper.database.dao.PlannedMatchesDAO;
import stos.keeper.database.dao.PlayerDAO;
import stos.keeper.database.PostgresDataSourceImpl;
import stos.keeper.model.planned_matches.FootballMatch;
import stos.keeper.model.planned_matches.Score;
import stos.keeper.sparkServer.json.JsonTransformer;
import stos.keeper.sparkServer.api.routes.LoginPlayerRoute;
import stos.keeper.sparkServer.api.routes.RegisterPlayerRoute;
import stos.keeper.sparkServer.security.DigestKeyChest;
import stos.keeper.sparkServer.security.TokenGenerator;

import java.util.List;

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
        TokenGenerator tokenGenerator = new TokenGenerator(DigestKeyChest.getInstance());
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

        post(RegisterPlayerRoute.PATH, new RegisterPlayerRoute(transformer, playerDAO, tokenGenerator));

        put(LoginPlayerRoute.PATH, new LoginPlayerRoute(transformer, playerDAO, tokenGenerator));
    }

}

