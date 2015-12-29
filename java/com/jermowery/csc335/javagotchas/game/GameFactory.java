package com.jermowery.csc335.javagotchas.game;

import com.jermowery.csc335.javagotchas.gamedecider.GameDecider;
import com.jermowery.csc335.javagotchas.gamedecider.GameDeciderFactory;
import com.jermowery.csc335.javagotchas.logic.Score;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;
import com.jermowery.csc335.javagotchas.questionselector.QuestionSelector;
import com.jermowery.csc335.javagotchas.questionselector.QuestionSelectorFactory;

/**
 * Created by Jeremy on 12/28/2015.
 */
public class GameFactory {
    public static final int MAX_SCORE = 10;
    public static final int INITTIAL_SCORE = 0;

    public static Game getGame(GameSettings gameSettings, Data data) {
        Score score = new Score(MAX_SCORE, INITTIAL_SCORE);
        QuestionSelector questionSelector
                = QuestionSelectorFactory.getQuestionSelector(gameSettings, data);
        GameDecider gameDecider = GameDeciderFactory.getGameDecider(gameSettings);
        return new Game(questionSelector, score, gameDecider);
    }
}
