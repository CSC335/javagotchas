package com.jermowery.csc335.javagotchas.gamedecider;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
public class UnendingGameDecider implements GameDecider {

    @Override
    public boolean isOver(int numTurnsTaken) {
        return false;
    }
}
