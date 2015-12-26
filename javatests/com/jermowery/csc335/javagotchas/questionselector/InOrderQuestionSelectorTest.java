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

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by jeremy on 12/20/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class InOrderQuestionSelectorTest {
    private Question q1;
    private Question q2;
    private QuestionSelector testQuestionSelector;
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
        Data data = Data.newBuilder()
                .addQuestion(q1)
                .addQuestion(q2)
                .build();
        this.testQuestionSelector = new InOrderQuestionSelector(data);
    }

    @Test
    public void testMoveToPreviousQuestionAtBeginning() {
        assertThat(this.testQuestionSelector.moveToPreviousQuestion()).isEqualTo(this.testQuestionSelector);
    }

    @Test
    public void testMoveToNextQuestionInitial() {
        assertThat(this.testQuestionSelector.moveToNextQuestion()).isEqualTo(this.testQuestionSelector);
    }

    @Test
    public void testGetCurrentQuestionInitial() {
        assertThat(this.testQuestionSelector.getCurrentQuestion()).isEqualTo(q1);
    }

    @Test
    public void testGetCurrentQuestionSecond() {
        assertThat(this.testQuestionSelector.moveToNextQuestion().getCurrentQuestion()).isEqualTo(q2);
    }

    @Test
    public void testGetCurrentQuestionPreviousFromInitial() {
        assertThat(this.testQuestionSelector.moveToPreviousQuestion().getCurrentQuestion()).isEqualTo(q2);
    }

    @Test
    public void testMoveToPreviousQuestionCircular() {
        assertThat(this.testQuestionSelector.moveToPreviousQuestion().moveToPreviousQuestion())
                .isEqualTo(this.testQuestionSelector);
    }

    @Test
    public void testMoveToNextQuestionCircular() {
        assertThat(this.testQuestionSelector.moveToNextQuestion().moveToNextQuestion())
                .isEqualTo(this.testQuestionSelector);
    }

    @Test
    public void testGetCurrentQuestionPreviousCircular() {
        assertThat(this.testQuestionSelector.moveToPreviousQuestion().moveToPreviousQuestion().getCurrentQuestion())
                .isEqualTo(q1);
    }

    @Test
    public void testGetCurrentQuestionNextCircular() {
        assertThat(this.testQuestionSelector.moveToNextQuestion().moveToNextQuestion().getCurrentQuestion())
                .isEqualTo(q1);
    }

    @Test
    public void testGetCurrentQuestionNextPreviousInverse() {
        assertThat(this.testQuestionSelector.moveToNextQuestion().moveToPreviousQuestion().getCurrentQuestion())
                .isEqualTo(q1);
    }

    @Test
    public void testGetCurrentQuestionPreviousNextInverse() {
        assertThat(this.testQuestionSelector.moveToPreviousQuestion().moveToNextQuestion().getCurrentQuestion())
                .isEqualTo(q1);
    }
}

