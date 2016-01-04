package com.jermowery.csc335.javagotchas.gamedecider;

import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class GameDeciderFactory {
    public static final int MAX_TURNS = 10;

    public static GameDecider getGameDecider(GameSettings gameSettings) {
        switch (gameSettings.gameDeciderType) {
            case GameSettingsProto.TURNS:
                return getTurnsTakenGameDecider(MAX_TURNS);
            case GameSettingsProto.UNENDING:
                return getUnendingGameDecider();
            default:
                throw new IllegalArgumentException("Game decider type of GameSettings must be valid");
        }
    }

    public static GameDecider getUnendingGameDecider() {
        return new UnendingGameDecider();
    }

    public static GameDecider getTurnsTakenGameDecider(int maxTurns) {
        return new TurnsTakenGameDecider(maxTurns);
    }
}
