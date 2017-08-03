package stos.keeper.sparkServer;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class Bootstrap {

    public static void main(String[] args) {
        staticFileLocation("/public");
        get("/", (req, response) -> "Hello World!");
    }
}
