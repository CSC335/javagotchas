package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

import java.util.Random;

/**
 * Created by jeremy on 12/19/15.
 */
public class RandomQuestionSelector extends QuestionSelector {
    private Random random;
    private int currentIndex;

    public RandomQuestionSelector(Data data, Random random) {
        super(data);
        this.random = random;
        this.moveToNextQuestion();
    }

    @Override
    public QuestionSelector moveToNextQuestion() {
        this.currentIndex = this.random.nextInt(this.getData().question.length);
        return this;
    }

    @Override
    public QuestionSelector moveToPreviousQuestion() {
        return null;
    }

    @Override
    public Question getCurrentQuestion() {
        return this.getData().question[this.currentIndex];
    }
}
