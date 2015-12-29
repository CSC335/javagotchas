package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;

import java.util.Random;

/**
 * Created by jeremy on 12/19/15.
 */
public class QuestionSelectorFactory {
    public static QuestionSelector getRandomQuestionSelector(Data data, Random random) {
        return new RandomQuestionSelector(data, random);
    }

    public static QuestionSelector getInOrderQuestionSelector(Data data) {
        return new InOrderQuestionSelector(data);
    }

    public static QuestionSelector getQuestionSelector(GameSettings gameSettings, Data data) {
        switch (gameSettings.questionSelectorType) {
            case GameSettingsProto.IN_ORDER:
                return getInOrderQuestionSelector(data);
            case GameSettingsProto.RANDOM:
                return getRandomQuestionSelector(data, new Random());
            default:
                throw new IllegalArgumentException("Question selector type of GameSettings must be valid");
        }
    }
}
