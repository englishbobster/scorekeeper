package stos.keeper.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stos.keeper.model.planned_matches.FootballMatch;
import stos.keeper.model.planned_matches.MatchType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MatchLoader {
    private static final char CSV_SEPARATOR = ',';
    private static final char CSV_QUOTE = '"';
    private static final char EOL = '\n';
    private Logger LOG = LoggerFactory.getLogger(MatchLoader.class);

    private String file;

    public MatchLoader(String file) {
        this.file = file;
    }

    public List<FootballMatch> getFootballMatches() {
        String line;
        List<FootballMatch> matches = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                Optional<FootballMatch> match = parseLine(line);
                match.ifPresent(matches::add);
            }
        } catch (IOException e) {
            LOG.info("Failed to read file {} and load matches. Failed with error: {}", file, e.getMessage());
            throw new RuntimeException("Unable to load base matches from file.");
        }
        return matches;
    }

    Optional<FootballMatch> parseLine(String line) {
        LOG.info("Transforming entry: {}", line);
        if (line == null || line.isEmpty()) {
            return Optional.empty();
        }
        if (line.trim().startsWith("#")) {
            return Optional.empty();
        }
        List<String> stringList = splitLine(line.trim());
        ZonedDateTime matchTime = createMatchTime(stringList.get(2), stringList.get(3));

        MatchType matchType = null;

        if(isInEnum(stringList.get(9), MatchType.class)){
            matchType = MatchType.valueOf(stringList.get(9));
        }

        return Optional.of(FootballMatch.builder().id(Integer.parseInt(stringList.get(0))).time(matchTime)
                .matchType(matchType).arena(stringList.get(8))
                .teams(stringList.get(4), stringList.get(7)).build());
    }

    ZonedDateTime createMatchTime(String monthDateYear, String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy H:mm")
                .withZone(ZoneId.systemDefault());
        return ZonedDateTime.parse(monthDateYear + " " + time, dateTimeFormatter);
    }

    List<String> splitLine(String line) {
        List<String> parsedStrings = new ArrayList<>();
        char[] chars = line.toCharArray();
        boolean insideQuote = false;
        StringBuilder stringValue = new StringBuilder();
        for (char c : chars) {
            if(insideQuote) {
                if(c == CSV_SEPARATOR){
                    stringValue.append(c);
                    continue;
                }
                if (c == CSV_QUOTE) {
                    insideQuote = false;
                    continue;
                }
            }
            if(!insideQuote) {
                if(c == CSV_QUOTE) {
                    insideQuote = true;
                    continue;
                }
            }
            if (c == CSV_SEPARATOR) {
                parsedStrings.add(stringValue.toString().trim());
                stringValue.setLength(0);
                insideQuote = false;
            } else if (c == EOL) {
                break;
            } else {
                stringValue.append(c);
            }
        }
        parsedStrings.add(stringValue.toString().trim());
        return parsedStrings;
    }

    private boolean isInEnum(String value, Class<MatchType> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).anyMatch(e -> e.name().endsWith(value));
    }
}