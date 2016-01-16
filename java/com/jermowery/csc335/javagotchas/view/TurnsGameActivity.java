package com.jermowery.csc335.javagotchas.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.jermowery.csc335.javagotchas.logic.UpdateState;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class TurnsGameActivity extends GameActivity {

    private List<Button> answerButtons;
    private Button nextButton;
    private Button lastClicked;
    private boolean isSetup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isSetup = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void startGame() {
        if (!isSetup) {
            this.setupNextButton();
            this.setupAnswerButtons();
            isSetup = true;
        }
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
        TextView questionText = (TextView) findViewById(R.id.questionText);
        switch (updateState) {
            case CHANGE_QUESTION:
                this.nextButton.setVisibility(Button.INVISIBLE);
                Question question = this.game.getCurrentQuestion();
                questionText.setText("#" + question.id + " " + question.text);
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
                this.player.turnsGameStats.questionsAttempted++;
                int index = this.answerButtons.indexOf(this.lastClicked);
                if (this.game.getCurrentQuestion().answer[index].isCorrect) {
                    this.lastClicked.setBackgroundColor(Color.parseColor("#1B5E20"));
                    this.player.turnsGameStats.questionsCorrect++;
                    this.player.turnsGameStats.currentCorrectStreak++;
                    this.player.turnsGameStats.currentIncorrectStreak = 0;
                    this.incrementAchievement(getString(R.string.achievement_the_rick), 1);
                    if (this.player.turnsGameStats.currentCorrectStreak > this.player.turnsGameStats.maxCorrectStreak) {
                        this.player.turnsGameStats.maxCorrectStreak = this.player.turnsGameStats.currentCorrectStreak;
                        this.incrementAchievement(getString(R.string.achievement_the_rick_roll), 1);
                    }
                } else {
                    this.lastClicked.setBackgroundColor(Color.parseColor("#B71C1C"));
                    this.player.turnsGameStats.questionsIncorrect++;
                    this.player.turnsGameStats.currentIncorrectStreak++;
                    this.player.turnsGameStats.currentCorrectStreak = 0;
                    this.incrementAchievement(getString(R.string.achievement_impressive_loss), 1);
                    if (this.player.turnsGameStats.currentIncorrectStreak > this.player.turnsGameStats.maxIncorrectStreak) {
                        this.player.turnsGameStats.maxIncorrectStreak = this.player.turnsGameStats.currentIncorrectStreak;
                        this.incrementAchievement(getString(R.string.achievement_the_jeremy), 1);
                    }
                }

                questionText.setText(
                        questionText.getText() + "\nExplanation: " + game.getCurrentQuestion().explanation);
                this.nextButton.setVisibility(Button.VISIBLE);
                break;
        }
    }

    public void disableAnswerButtons() {
        for (Button b : this.answerButtons) {
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    private class AnswerClickListener implements View.OnClickListener {

        private int index;

        public AnswerClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            TurnsGameActivity.this.disableAnswerButtons();
            TurnsGameActivity.this.lastClicked = (Button) view;
            TurnsGameActivity.this.game.selectAnswer(this.index);
        }
    }
}
