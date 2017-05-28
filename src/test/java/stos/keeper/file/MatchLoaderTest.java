package stos.keeper.file;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MatchLoaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        File testCsv = folder.newFile("testCsv.csv");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(testCsv))){
            writer.write("1,Thu,\"Jun 12, 2014\",21:00,Brazil,,,Croatia,Sao Paulo, A");
            writer.newLine();
            writer.write("2,Fri,\"Jun 13, 2014\",17:00,Mexico,,,Cameroon,Natal, A");
            writer.newLine();
            writer.write("3, Fri,\"Jun 13, 2014\",20:00,Spain,0,0,Netherlands,Salvador,B");
            writer.newLine();
            writer.flush();
        }
    }

    @Test
    public void load_matches_from_file() {
        MatchLoader matchLoader = new MatchLoader(folder.getRoot().toPath().toString() + "/testCsv.csv");
        List<String> matchRows = matchLoader.getFootballMatches();
        assertThat(matchRows.size(), is(3));
    }
}