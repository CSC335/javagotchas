package com.jermowery.csc335.javagotchas.gamedecider;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class TurnsTakenGameDecider implements GameDecider {
    private int maxTurnsTaken;

    public TurnsTakenGameDecider(int maxTurnsTaken) {
        this.maxTurnsTaken = maxTurnsTaken;
    }

    @Override
    public boolean isOver(int numTurnsTaken) {
        return numTurnsTaken >= this.maxTurnsTaken;
    }
}
