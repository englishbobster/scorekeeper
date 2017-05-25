package stos.keeper.model;

import java.time.ZonedDateTime;

public class FootballMatch {
    private ZonedDateTime matchTime;
    private String arena;
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    private FootballMatch(ZonedDateTime matchTime, String arena, String homeTeam, String awayTeam, int homeScore, int awayScore) {
        this.matchTime = matchTime;
        this.arena = arena;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public static MatcherBuilder builder() {
        return new MatcherBuilder();
    }

    public String getFinalScoreAsString() {
        return homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam;
    }

    public static class MatcherBuilder {
        private ZonedDateTime matchTime;
        private String arena;
        private String homeTeam;
        private String awayTeam;

        public FootballMatch build() {
            return new FootballMatch(matchTime, arena, homeTeam, awayTeam, 0, 0);
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
    }
}
