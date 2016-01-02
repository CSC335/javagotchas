package com.jermowery.csc335.javagotchas.view;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.jermowery.csc335.javagotchas.logic.UpdateState;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Jeremy on 12/28/2015.
 */
public class ViewGameActivity extends GameActivity {
    private List<Button> answerButtons;

    @Override
    protected void startGame() {
        this.setUpAnswerButtons();
        this.setUpNavigationButtons();
        this.setUpDrownDown();
        this.update(this.game, UpdateState.CHANGE_QUESTION);
    }

    private void setUpAnswerButtons() {
        this.answerButtons = new ArrayList<>();
        ViewGroup buttonGroup = (ViewGroup) findViewById(R.id.answerGroup);
        for (int i = 0; i < buttonGroup.getChildCount(); i++) {
            answerButtons.add((Button) buttonGroup.getChildAt(i));
            answerButtons.get(i).setEnabled(false);
            answerButtons.get(i).setTextColor(Color.BLACK);
        }
    }

    private void setUpDrownDown() {
        Spinner questionSelector = (Spinner) findViewById(R.id.questionSelector);
        Integer[] questionIndicies = new Integer[this.game.getTotalNumberOfQuestions()];
        for (int i = 0; i < questionIndicies.length; i++) {
            questionIndicies[i] = i + 1;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this.getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                questionIndicies);
        questionSelector.setAdapter(adapter);
        questionSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ViewGameActivity.this.game.goToQuestion(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        questionSelector.setVisibility(Spinner.VISIBLE);
    }

    private void setUpNavigationButtons() {
        final Button previousButton = (Button) findViewById(R.id.previousQuestionButton);
        previousButton.setVisibility(Button.VISIBLE);
        final Button nextButton = (Button) findViewById(R.id.nextQuestionButton);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == previousButton) {
                    ViewGameActivity.this.game.goToPreviousQuestion();
                } else if (view == nextButton) {
                    ViewGameActivity.this.game.goToNextQuestion();
                }
            }
        };
        previousButton.setOnClickListener(listener);
        nextButton.setOnClickListener(listener);
    }

    @Override
    public void update(Observable observable, Object o) {
        UpdateState updateState = (UpdateState) o;
        if (updateState == UpdateState.CHANGE_QUESTION) {
            Question question = this.game.getCurrentQuestion();
            TextView questionText = (TextView) findViewById(R.id.questionText);
            questionText.setText("#" + question.id + " " + question.text + "\n\tExplanation: " + question.explanation);
            for (int i = 0; i < this.answerButtons.size(); i++) {
                Button b = this.answerButtons.get(i);
                if (i < question.answer.length) {
                    b.setText(question.answer[i].text);
                    b.setVisibility(Button.VISIBLE);
                    b.setBackgroundColor(
                            question.answer[i].isCorrect ? Color.GREEN : Color.RED
                    );
                } else {
                    b.setVisibility(Button.INVISIBLE);
                }
            }
        }
    }
}
