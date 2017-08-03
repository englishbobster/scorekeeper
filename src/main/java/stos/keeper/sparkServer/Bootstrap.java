package stos.keeper.sparkServer;

import static spark.Spark.get;

public class Bootstrap {

    public static void main(String[] args) {
        get("/", (req, response) -> "Hello World!");
    }
}
