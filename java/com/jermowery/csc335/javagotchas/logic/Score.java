package com.jermowery.csc335.javagotchas.logic;

import java.io.Serializable;

/**
 * Created by jeremy on 12/19/15.
 */
public class Score implements Serializable {
    private static final int MIN_MAX_SCORE = 0;
    private int maxScore;
    private int currentScore;

    public Score(int maxScore, int initialScore) {
        if (maxScore < MIN_MAX_SCORE) {
            throw new IllegalArgumentException("Max score may not be smaller than " + MIN_MAX_SCORE + ".");
        }
        if (initialScore > maxScore) {
            throw new IllegalArgumentException("Initial score may not be larger than the maximum score.");
        }
        this.maxScore = maxScore;
        this.currentScore = initialScore;
    }

    public int getMaxScore() {
        return this.maxScore;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public void incrementScore(int delta) {
        this.currentScore = Math.min(this.currentScore + delta, this.maxScore);
    }

    @Override
    public boolean equals(Object other) {
        return this.maxScore == ((Score) other).getMaxScore() && this.currentScore == ((Score) other).getCurrentScore();
    }
}
