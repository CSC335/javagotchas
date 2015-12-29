package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

import java.io.Serializable;

/**
 * Created by jeremy on 12/19/15.
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
}
