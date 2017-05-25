import static spark.Spark.get;

public class ScoreKeeper {

    public static String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        get("/", ((request, response) -> getGreeting()));
    }
}
