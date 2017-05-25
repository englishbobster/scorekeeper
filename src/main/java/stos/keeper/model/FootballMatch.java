package stos.keeper.model;

import java.time.ZonedDateTime;

public class FootballMatch {
    private ZonedDateTime matchTime;
    private String arena;
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private boolean fullTime;

    private FootballMatch(ZonedDateTime matchTime, String arena, String homeTeam, String awayTeam) {
        this.matchTime = matchTime;
        this.arena = arena;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.fullTime = false;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public static MatcherBuilder builder() {
        return new MatcherBuilder();
    }

    public String getFinalScoreAsString() {
        return homeTeam + " " + homeScore + " - " + awayScore + " " + awayTeam;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFinalScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
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
            if (this.matchTime.equals(that.matchTime)
                    && this.homeTeam.equals(that.homeTeam)
                    && this.awayTeam.equals(that.awayTeam)) {
                return true;
            }
            return false;
        }
    }

    public static class MatcherBuilder {
        private ZonedDateTime matchTime;
        private String arena;
        private String homeTeam;
        private String awayTeam;

        public FootballMatch build() {
            return new FootballMatch(matchTime, arena, homeTeam, awayTeam);
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
