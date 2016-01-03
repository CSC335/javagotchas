package com.jermowery.csc335.javagotchas.gamedecider;

import java.io.Serializable;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
public interface GameDecider extends Serializable {
    boolean isOver(int numTurnsTaken);
}
