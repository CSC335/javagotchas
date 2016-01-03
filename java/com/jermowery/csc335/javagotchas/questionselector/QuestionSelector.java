package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

import java.io.Serializable;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
public abstract class QuestionSelector implements Serializable {
    private Data data;

    public QuestionSelector(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public abstract QuestionSelector moveToNextQuestion();

    public abstract QuestionSelector moveToPreviousQuestion();

    public abstract Question getCurrentQuestion();

    public abstract QuestionSelector goToQuestion(int index);

    public int getTotalNumberOfQuestions() {
        return this.data.question.length;
    }
}
