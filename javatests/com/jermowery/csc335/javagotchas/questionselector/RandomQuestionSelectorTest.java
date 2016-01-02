package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Answer;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.view.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by jeremy on 12/19/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class RandomQuestionSelectorTest {
    private Question q1;
    private Question q2;
    private Data data;

    @Before
    public void setUp() {
        this.q1 = new Question();
        this.q1.id = 0;
        this.q1.text = "Question 0";
        this.q1.explanation = "Explanation 0";
        this.q1.answer = new Answer[2];
        this.q1.answer[0] = new Answer();
        this.q1.answer[0].isCorrect = true;
        this.q1.answer[0].text = "Answer 1";
        this.q1.answer[1] = new Answer();
        this.q1.answer[1].isCorrect = false;
        this.q1.answer[1].text = "Answer 2";
        this.q2 = new Question();
        this.q2.id = 1;
        this.q2.text = "Question 1";
        this.q2.explanation = "Explanation 1";
        this.q2.answer = new Answer[2];
        this.q2.answer[0] = new Answer();
        this.q2.answer[0].isCorrect = false;
        this.q2.answer[0].text = "Answer 1";
        this.q2.answer[1] = new Answer();
        this.q2.answer[1].isCorrect = true;
        this.q2.answer[1].text = "Answer 2";
        this.data = new Data();
        data.question = new Question[2];
        data.question[0] = this.q1;
        data.question[1] = this.q2;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMoveToPreviousQuestionInitialThrowsException() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random());
        assertThat(testQuestionSelector.moveToPreviousQuestion()).isNotNull();
    }

    @Test
    public void testMoveToNextQuestionInitial() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random());
        assertThat(testQuestionSelector.moveToNextQuestion()).isEqualTo(testQuestionSelector);
    }

    @Test
    public void testGetCurrentQuestionInitial() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random() {
            @Override
            public int nextInt(int range) {
                return 1;
            }
        });
        assertThat(testQuestionSelector.getCurrentQuestion()).isEqualTo(this.q2);
    }

    @Test
    public void testGetCurrentQuestionAfterNext() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random() {
            @Override
            public int nextInt(int range) {
                return 1;
            }
        });
        assertThat(testQuestionSelector.moveToNextQuestion().getCurrentQuestion()).isEqualTo(this.q2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMoveToPreviousAfterMoveToNext() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random());
        assertThat(testQuestionSelector.moveToNextQuestion().moveToPreviousQuestion()).isNotNull();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGoToQuestionThrowsException() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random());
        assertThat(testQuestionSelector.goToQuestion(0)).isNotNull();
    }
}
