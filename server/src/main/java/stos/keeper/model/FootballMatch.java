package stos.keeper.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FootballMatch {
    private int id;
    private ZonedDateTime matchTime;
    private String arena;
    private String homeTeam;
    private String awayTeam;
    private Score score;
    private boolean fullTime;
    private MatchType matchType;

    private FootballMatch(int id, ZonedDateTime matchTime, String arena, String homeTeam, String awayTeam, MatchType matchType) {
        this.id = id;
        this.matchTime = matchTime;
        this.arena = arena;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.score = new Score();
        this.fullTime = false;
        this.matchType = matchType;
    }

    public Score getScore() {
        return score;
    }

    public static MatcherBuilder builder() {
        return new MatcherBuilder();
    }

    public String getFinalScoreAsString() {
        return homeTeam + " " + score.toString() + " " + awayTeam;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFinalScore(int homeScore, int awayScore) {
        score.setScore(homeScore, awayScore);
        this.fullTime = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FootballMatch)) {
            return false;
        } else {
            FootballMatch that = (FootballMatch) obj;
            if (this.id == that.id
                    && this.matchTime.equals(that.matchTime)
                    && this.arena.equals(that.arena)
                    && this.homeTeam.equals(that.homeTeam)
                    && this.awayTeam.equals(that.awayTeam)
                    && this.matchType == that.matchType) {

                return true;
            }
        }
        return false;
    }


    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + matchTime.hashCode();
        result = 31 * result + arena.hashCode();
        result = 31 * result + homeTeam.hashCode();
        result = 31 * result + awayTeam.hashCode();
        result = 31 * result + matchType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MatchId: " + id + " ");
        builder.append("(");
        builder.append(matchTime.format(DateTimeFormatter.RFC_1123_DATE_TIME));
        builder.append(") ");
        builder.append(arena);
        builder.append(", ");
        builder.append(matchType.toString());
        builder.append(", [");
        builder.append(getFinalScoreAsString());
        builder.append("]");
        builder.append("\n");
        return  builder.toString();
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public int getId() {
        return id;
    }

    public ZonedDateTime getMatchTime() {
        return matchTime;
    }

    public String getArena() {
        return arena;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public static class MatcherBuilder {
        private int id;
        private ZonedDateTime matchTime;
        private String arena;
        private String homeTeam;
        private String awayTeam;
        private MatchType matchType;

        public FootballMatch build() {
            return new FootballMatch(id, matchTime, arena, homeTeam, awayTeam, matchType);
        }

        public MatcherBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MatcherBuilder time(ZonedDateTime dateTime) {
            this.matchTime = dateTime;
            return this;
        }

        public MatcherBuilder teams(String homeTeam, String awayTeam) {
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
            return this;
        }

        public MatcherBuilder arena(String arena) {
            this.arena = arena;
            return this;
        }

        public MatcherBuilder matchType(MatchType matchType) {
            this.matchType = matchType;
            return this;
        }
    }
}
