package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Answer;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;

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
        this.q1 = Question.newBuilder()
                .setId(0)
                .setText("Question 0")
                .setExplanation("Explanation 0")
                .addAnswer(
                        Answer.newBuilder()
                                .setText("Answer 1")
                                .setIsCorrect(true)
                )
                .addAnswer(
                        Answer.newBuilder()
                                .setText("Answer 2")
                                .setIsCorrect(false)
                ).build();
        this.q2 = Question.newBuilder()
                .setId(1)
                .setText("Question 1")
                .setExplanation("Explanation 1")
                .addAnswer(
                        Answer.newBuilder()
                                .setText("Answer 1")
                                .setIsCorrect(false)
                )
                .addAnswer(
                        Answer.newBuilder()
                                .setText("Answer 2")
                                .setIsCorrect(true)
                ).build();
        this.data = Data.newBuilder()
                .addQuestion(q1)
                .addQuestion(q2)
                .build();
    }

    @Test
    public void testMoveToPreviousQuestionInitialNull() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random());
        assertThat(testQuestionSelector.moveToPreviousQuestion()).isNull();
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

    @Test
    public void testMoveToPreviousAfterMoveToNext() {
        QuestionSelector testQuestionSelector = new RandomQuestionSelector(this.data, new Random());
        assertThat(testQuestionSelector.moveToNextQuestion().moveToPreviousQuestion()).isNull();
    }
}
