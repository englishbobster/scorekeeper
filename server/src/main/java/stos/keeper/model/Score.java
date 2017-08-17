package stos.keeper.model;

public class Score {
    private int homeScore;
    private int awayScore;

    public Score(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public Score() {
        this(0, 0);
    }

    public void setScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof Score)) {
            return false;
        } else {
            Score that = (Score) object;
            if (this.homeScore == that.homeScore && this.awayScore == that.awayScore) {
                return true;
            }
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + homeScore;
        result = 31 * result + awayScore;
        return result;
    }

    @Override
    public String toString() {
        return "[ " + homeScore + " - " + awayScore + " ]";
    }
}

