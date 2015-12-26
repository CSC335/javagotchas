package com.jermowery.csc335.javagotchas.game;

import com.jermowery.csc335.javagotchas.gamedecider.GameDecider;
import com.jermowery.csc335.javagotchas.logic.Score;
import com.jermowery.csc335.javagotchas.logic.UpdateState;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.questionselector.QuestionSelector;

import java.util.Observable;

/**
 * Created by jeremy on 12/18/15.
 */
public class Game extends Observable {
    private static final int SCORE_DELTA = 1;
    private QuestionSelector questionSelector;
    private Question currentQuestion;
    private Score score;
    private int numTurnsTaken;
    private GameDecider gameDecider;

    public Game(QuestionSelector questionSelector, Score score, GameDecider gameDecider) {
        this.questionSelector = questionSelector;
        this.score = score;
        this.currentQuestion = this.questionSelector.getCurrentQuestion();
        this.gameDecider = gameDecider;
        this.numTurnsTaken = 0;
    }

    public void selectAnswer(int index) {
        UpdateState state;
        if (this.currentQuestion.answer[index].isCorrect) {
            state = UpdateState.CORRECT_ANSWER;
            this.score.incrementScore(SCORE_DELTA);
        } else {
            state = UpdateState.INCORRECT_ANSWER;
        }
        this.setChanged();
        this.notifyObservers(state);
    }

    public void goToNextQuestion() {
        if (!this.isOver()) {
            this.numTurnsTaken++;
            this.currentQuestion = this.questionSelector.moveToNextQuestion().getCurrentQuestion();
            this.setChanged();
            this.notifyObservers(UpdateState.CHANGE_QUESTION);
        }
    }

    public void goToPreviousQuestion() {
        if (this.questionSelector.moveToPreviousQuestion() != null) {
            this.currentQuestion = this.questionSelector.moveToPreviousQuestion().getCurrentQuestion();
            this.setChanged();
            this.notifyObservers(UpdateState.CHANGE_QUESTION);
        }
    }

    public Score getScore() {
        return new Score(this.score.getMaxScore(), this.score.getCurrentScore());
    }

    public boolean isOver() {
        return this.gameDecider.isOver(this.numTurnsTaken);
    }

    public int getNumTurnsTaken() {
        return this.numTurnsTaken;
    }

    public Question getCurrentQuestion() {
        return this.currentQuestion;
    }


}
