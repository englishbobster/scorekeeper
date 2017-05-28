package stos.keeper.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchLoader {

    private String file;

    public MatchLoader(String file) {
        this.file = file;
    }

    public List<String> getFootballMatches() {
        String line;
        List<String> lines = new ArrayList<>();
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}