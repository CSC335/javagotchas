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

import static com.google.common.truth.Truth.assertThat;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class InOrderQuestionSelectorTest {
    private Question q1;
    private Question q2;
    private QuestionSelector testQuestionSelector;

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
        Data data = new Data();
        data.question = new Question[2];
        data.question[0] = this.q1;
        data.question[1] = this.q2;
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

    @Test
    public void testGoToQuestionValidIndex() {
        assertThat(this.testQuestionSelector.goToQuestion(1)).isEqualTo(testQuestionSelector);
        assertThat(this.testQuestionSelector.getCurrentQuestion().toString()).isEqualTo(this.q2.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGoToQuestionTooSmallIndex() {
        this.testQuestionSelector.goToQuestion(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGoToQuestionTooLargeIndex() {
        this.testQuestionSelector.goToQuestion(9999);
    }
}

