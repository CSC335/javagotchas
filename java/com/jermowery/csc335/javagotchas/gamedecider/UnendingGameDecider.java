package com.jermowery.csc335.javagotchas.gamedecider;

/**
 * Created by jeremy on 12/21/15.
 */
public class UnendingGameDecider implements GameDecider {

    @Override
    public boolean isOver(int numTurnsTaken) {
        return false;
    }
}
