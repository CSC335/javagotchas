package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

import java.util.Random;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
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
        throw new UnsupportedOperationException("Move to previous question not supported by RandomQuestionSelector.");
    }

    @Override
    public Question getCurrentQuestion() {
        return this.getData().question[this.currentIndex];
    }

    @Override
    public QuestionSelector goToQuestion(int index) {
        throw new UnsupportedOperationException("Go to a question by index not supported by RandomQuestionSelector.");
    }
}
