package com.jermowery.csc335.javagotchas.gamedecider;

/**
 * Created by jeremy on 12/21/15.
 */
public class TurnsTakenGameDecider implements GameDecider{
    private int maxTurnsTaken;

    public TurnsTakenGameDecider(int maxTurnsTaken) {
        this.maxTurnsTaken = maxTurnsTaken;
    }

    @Override
    public boolean isOver(int numTurnsTaken) {
        return numTurnsTaken >= this.maxTurnsTaken;
    }
}
