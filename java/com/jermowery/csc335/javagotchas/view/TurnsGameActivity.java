package com.jermowery.csc335.javagotchas.view;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.jermowery.csc335.javagotchas.R;
import com.jermowery.csc335.javagotchas.logic.UpdateState;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Jeremy on 12/28/2015.
 */
public class TurnsGameActivity extends GameActivity {

    private List<Button> answerButtons;
    private Button nextButton;
    private Button lastClicked;

    @Override
    protected void startGame() {
        this.setupNextButton();
        this.setupAnswerButtons();
        this.update(this.game, UpdateState.CHANGE_QUESTION);
    }

    private void setupNextButton() {
        this.nextButton = (Button) findViewById(R.id.nextQuestionButton);
        this.nextButton.setVisibility(Button.INVISIBLE);
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TurnsGameActivity.this.game.isOver()) {
                    TurnsGameActivity.this.game.goToNextQuestion();
                } else {
                    Intent summaryIntent = new Intent(
                            TurnsGameActivity.this.getApplicationContext(), GameSummaryActivity.class);
                    summaryIntent.putExtra(getString(R.string.score), TurnsGameActivity.this.game.getScore());
                    startActivity(summaryIntent);
                    finish();
                }

            }
        });
    }

    @Override
    public void update(Observable observable, Object o) {
        UpdateState updateState = (UpdateState) o;
        switch (updateState) {
            case CHANGE_QUESTION:
                this.nextButton.setVisibility(Button.INVISIBLE);
                Question question = this.game.getCurrentQuestion();
                TextView questionText = (TextView) findViewById(R.id.questionText);
                questionText.setText("#" + question.id + question.text);
                for (int i = 0; i < this.answerButtons.size(); i++) {
                    Button b = this.answerButtons.get(i);
                    b.setEnabled(true);
                    b.setBackgroundColor(Color.GRAY);
                    b.setTextColor(Color.WHITE);
                    if (i < question.answer.length) {
                        b.setText(question.answer[i].text);
                        b.setVisibility(Button.VISIBLE);
                    } else {
                        b.setVisibility(Button.INVISIBLE);
                    }
                }
                break;
            case CORRECT_ANSWER:
            case INCORRECT_ANSWER:
                int index = this.answerButtons.indexOf(this.lastClicked);
                if (this.game.getCurrentQuestion().answer[index].isCorrect) {
                    this.lastClicked.setBackgroundColor(Color.GREEN);
                } else {
                    this.lastClicked.setBackgroundColor(Color.RED);
                }
                this.lastClicked.setTextColor(Color.BLACK);
                TextView explanationText = (TextView) findViewById(R.id.questionText);
                explanationText.setText(
                        explanationText.getText() + "\nExplanation: " + game.getCurrentQuestion().explanation);
                this.nextButton.setVisibility(Button.VISIBLE);
                break;
        }
    }

    public void disableButtons() {
        for (int i = 0; i < this.answerButtons.size(); i++) {
            Button b = this.answerButtons.get(i);
            b.setEnabled(false);
        }
    }

    public void setupAnswerButtons() {
        answerButtons = new ArrayList<>();
        ViewGroup buttonGroup = (ViewGroup) findViewById(R.id.answerGroup);
        for (int i = 0; i < buttonGroup.getChildCount(); i++) {
            Button b = (Button) buttonGroup.getChildAt(i);
            b.setOnClickListener(new AnswerClickListener(i));
            this.answerButtons.add(b);
        }
    }

    private class AnswerClickListener implements View.OnClickListener {

        private int index;

        public AnswerClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            TurnsGameActivity.this.disableButtons();
            TurnsGameActivity.this.lastClicked = (Button) view;
            TurnsGameActivity.this.game.selectAnswer(this.index);
        }
    }
}
