package com.jermowery.csc335.javagotchas.gamedecider;

import java.io.Serializable;

/**
 * Created by jeremy on 12/21/15.
 */
public interface GameDecider extends Serializable {
    boolean isOver(int numTurnsTaken);
}
