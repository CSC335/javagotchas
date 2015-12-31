package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

/**
 * Created by jeremy on 12/20/15.
 */
public class InOrderQuestionSelector extends QuestionSelector {
    private int currentIndex;

    public InOrderQuestionSelector(Data data) {
        super(data);
        this.currentIndex = 0;
    }

    @Override
    public QuestionSelector moveToNextQuestion() {
        this.currentIndex = (this.currentIndex + 1) % this.getData().question.length;
        return this;
    }

    @Override
    public QuestionSelector moveToPreviousQuestion() {
        this.currentIndex = this.currentIndex - 1 < 0 ? this.getData().question.length - 1 : this.currentIndex - 1;
        return this;
    }

    @Override
    public Question getCurrentQuestion() {
        return this.getData().question[this.currentIndex];
    }

    @Override
    public QuestionSelector goToQuestion(int index) {
        if (index >= 0 && index < this.getData().question.length) {
            this.currentIndex = index;
        } else {
            throw new IllegalArgumentException("Index must be >= 0 < " + this.getData().question.length + ".");
        }
        return this;
    }
}
