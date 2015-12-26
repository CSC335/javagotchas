package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;

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
}
