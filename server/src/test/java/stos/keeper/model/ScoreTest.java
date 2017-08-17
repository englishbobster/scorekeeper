package stos.keeper.model;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class ScoreTest {

    @Test
    public void scores_are_equal() {
        Score score1 = new Score(1, 1);
        Score score2 = new Score(1, 1);

        assertThat(score1, is(equalTo(score2)));
    }

    @Test
    public void scores_are_not_equal() throws Exception {
        Score score1 = new Score(1, 1);
        Score score2 = new Score(1, 3);

        assertThat(score1, is(not(equalTo(score2))));
    }

    @Test
    public void toString_is_printed_correctly() throws Exception {
        Score score1 = new Score(1, 7);

        String expectedString = "[ 1 - 7 ]";
        assertThat(score1.toString(), is(expectedString));
    }

    @Test
    public void change_a_score_from_nil_nil_to_some_other_value() {
        Score score1 = new Score();
        assertThat(score1.getHomeScore(), is(0));
        assertThat(score1.getAwayScore(), is(0));

        score1.setScore(5, 5);

        assertThat(score1.getHomeScore(), is(5));
        assertThat(score1.getAwayScore(), is(5));
    }

    @Test
    public void hashCode_gives_unique_values() throws Exception {
        int hash_3_2 = new Score(3, 2).hashCode();
        int hash_2_3 = new Score(2, 3).hashCode();

        assertThat(hash_2_3, is(not(equalTo(hash_3_2))));
    }

    @Test
    public void hashCode_gives_unique_values_again() throws Exception {
        int hash_0_1 = new Score(0, 1).hashCode();
        int hash_1_0 = new Score(1, 0).hashCode();

        assertThat(hash_1_0, is(not(equalTo(hash_0_1))));
    }

}