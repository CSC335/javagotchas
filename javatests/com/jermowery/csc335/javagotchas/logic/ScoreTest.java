package com.jermowery.csc335.javagotchas.logic;

import com.jermowery.csc335.javagotchas.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by jeremy on 12/19/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class ScoreTest {

    @Test
    public void testGetMaxScoreMaxValuePositive() {
        int maxScore = 10;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore.getMaxScore()).isEqualTo(maxScore);
    }

    @Test
    public void testGetCurrentScoreInitialValuePositive() {
        int maxScore = 10;
        int initialScore = 5;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore.getCurrentScore()).isEqualTo(initialScore);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithNegativeMaxScore() {
        int maxScore = -10;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore).isNotNull();
    }

    public void testGetMaxScoreWithZeroMaxScore() {
        int maxScore = 0;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore.getMaxScore()).isEqualTo(0);
    }

    @Test
    public void testGetCurrentScoreInitialValueZero() {
        int maxScore = 10;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore.getCurrentScore()).isEqualTo(initialScore);
    }

    @Test
    public void testGetCurrentScoreInitialValueNegative() {
        int maxScore = 10;
        int initialScore = 5;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore.getCurrentScore()).isEqualTo(initialScore);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithTooLargeInitialScore() {
        int maxScore = 10;
        int initialScore = 15;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore).isNotNull();
    }

    @Test
    public void testGetInitialScoreWithInitialEqualToMax() {
        int maxScore = 10;
        int initialScore = 10;
        Score testScore = new Score(maxScore, initialScore);
        assertThat(testScore.getCurrentScore()).isEqualTo(maxScore);
    }

    @Test
    public void testIncrementScoreBelowMax() {
        int maxScore = 10;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        testScore.incrementScore(9);
        assertThat(testScore.getCurrentScore()).isEqualTo(9);
    }

    @Test
    public void testIncrementScoreEqualsMax() {
        int maxScore = 10;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        testScore.incrementScore(10);
        assertThat(testScore.getCurrentScore()).isEqualTo(10);
    }


    @Test
    public void testIncrementScoreAboveMax() {
        int maxScore = 10;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        testScore.incrementScore(11);
        assertThat(testScore.getCurrentScore()).isEqualTo(10);
    }

    @Test
    public void testIncrementTwiceAboveMax() {
        int maxScore = 10;
        int initialScore = 0;
        Score testScore = new Score(maxScore, initialScore);
        testScore.incrementScore(6);
        testScore.incrementScore(6);
        assertThat(testScore.getCurrentScore()).isEqualTo(10);
    }

}

