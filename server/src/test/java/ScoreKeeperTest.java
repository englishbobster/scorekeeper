import org.junit.Test;
import static org.junit.Assert.*;

public class ScoreKeeperTest {
    @Test public void testAppHasAGreeting() {
        ScoreKeeper classUnderTest = new ScoreKeeper();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
