package stos.keeper.sparkServer;

import static spark.Spark.get;
import static spark.Spark.staticFiles;

public class Bootstrap {

    public static void main(String[] args) {
        staticFiles.externalLocation(System.getProperty("user.dir") + "/server/src/main/resources/public");
        get("/", (req, response) -> "Hello World!");
    }
}
