package com.jermowery.csc335.javagotchas.game;

import com.jermowery.csc335.javagotchas.gamedecider.GameDecider;
import com.jermowery.csc335.javagotchas.logic.Score;
import com.jermowery.csc335.javagotchas.logic.UpdateState;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Answer;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.questionselector.QuestionSelector;
import com.jermowery.csc335.javagotchas.view.BuildConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Observable;
import java.util.Observer;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.*;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class GameTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    private Question q1;
    @Mock
    private QuestionSelector mockQuestionSelector;
    @Mock
    private GameDecider mockGameDecider;
    @Mock
    private Score mockScore;
    @Mock
    private Observer mockObserver;
    @InjectMocks
    private Game testGame;

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

        when(mockQuestionSelector.moveToNextQuestion()).thenReturn(mockQuestionSelector);
        when(mockQuestionSelector.moveToPreviousQuestion()).thenReturn(mockQuestionSelector);
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(this.q1);
        when(mockScore.getMaxScore()).thenReturn(10);
        when(mockScore.getCurrentScore()).thenReturn(0);
        when(mockGameDecider.isOver(anyInt())).thenReturn(false);
        testGame.addObserver(mockObserver);
    }

    @Test
    public void testGameOverUsesGameDecider() {
        assertThat(testGame.isOver()).isFalse();
        verify(mockGameDecider).isOver(anyInt());
    }

    @Test
    public void testGameGetScoreInitial() {
        assertThat(testGame.getScore()).isEqualTo(mockScore);
    }

    @Test
    public void testTurnsTakenInitial() {
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(0);
    }

    @Test
    public void testGetNumTurnsTakenIncrementsOnSelectAnswer() {
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(0);
        testGame.goToNextQuestion();
        testGame.selectAnswer(0);
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(1);
        testGame.goToNextQuestion();
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(1);
        testGame.selectAnswer(1);
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(2);
        testGame.goToNextQuestion();
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(2);
    }

    @Test
    public void testMoveToNextQuestionUpdatesQuestion() {
        verify(mockQuestionSelector, never()).moveToNextQuestion();
        verify(mockQuestionSelector).getCurrentQuestion();
        testGame.goToNextQuestion();
        verify(mockQuestionSelector).moveToNextQuestion();
        verify(mockQuestionSelector, times(2)).getCurrentQuestion();
        testGame.goToNextQuestion();
        verify(mockQuestionSelector, times(2)).moveToNextQuestion();
        verify(mockQuestionSelector, times(3)).getCurrentQuestion();
    }

    @Test
    public void testMoveToPreviousQuestionUpdatesQuestion() {
        verify(mockQuestionSelector, never()).moveToPreviousQuestion();
        verify(mockQuestionSelector).getCurrentQuestion();
        testGame.goToPreviousQuestion();
        verify(mockQuestionSelector, times(1)).moveToPreviousQuestion();
        verify(mockQuestionSelector, times(2)).getCurrentQuestion();
        testGame.goToPreviousQuestion();
        verify(mockQuestionSelector, times(2)).moveToPreviousQuestion();
        verify(mockQuestionSelector, times(3)).getCurrentQuestion();
    }

    @Test
    public void testGetCurrentQuestionInitial() {
        // Move to next question first because at construction the mock value is not used
        testGame.goToNextQuestion();
        assertThat(testGame.getCurrentQuestion().toString()).isEqualTo(q1.toString());
    }

    @Test
    public void testGetCurrentQuestionAfterNextUpdate() {
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(q1);
        testGame.goToNextQuestion();
        assertThat(testGame.getCurrentQuestion()).isEqualTo(q1);
    }

    @Test
    public void testGetCurrentQuestionAfterPreviousUpdate() {
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(q1);
        testGame.goToPreviousQuestion();
        assertThat(testGame.getCurrentQuestion()).isEqualTo(q1);
    }

    @Test
    public void testCorrectAnswerIncreasesScore() {
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(q1);
        // Move to the next question because at construction the game has a null valuie for the question
        testGame.goToNextQuestion();
        testGame.selectAnswer(0);
        ArgumentMatcher<Integer> isPositive = new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object argument) {
                return (int) argument > 0;
            }
        };
        verify(mockScore).incrementScore(intThat(isPositive));
    }

    @Test
    public void testIncorrectAnswerDoesNotChangeScore() {
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(q1);
        testGame.goToNextQuestion();
        testGame.selectAnswer(1);
        verify(mockScore, never()).incrementScore(anyInt());
    }


    @Test
    public void testGoToNextQuestionUpdatesObservers() {
        when(mockGameDecider.isOver(anyInt())).thenReturn(false);
        testGame.goToNextQuestion();
        verify(mockObserver).update(eq(testGame), eq(UpdateState.CHANGE_QUESTION));
    }

    @Test
    public void testGoToNextQuestionAtEndDoesNotUpdateObervers() {
        when(mockGameDecider.isOver(anyInt())).thenReturn(true);
        testGame.goToNextQuestion();
        verify(mockObserver, never()).update(any(Observable.class), anyObject());
    }

    @Test
    public void testGoToPreviousQuestionAtEndDoesNotUpdateObervers() {
        when(mockGameDecider.isOver(anyInt())).thenReturn(true);
        testGame.goToPreviousQuestion();
        verify(mockObserver, never()).update(any(Observable.class), anyObject());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGoToPreviousQuestionThrowsExceptionWhenSelectorDoesNotSupportPrevious() {
        when(mockQuestionSelector.moveToPreviousQuestion()).thenThrow(UnsupportedOperationException.class);
        testGame.goToPreviousQuestion();
    }

    @Test
    public void testGoToPreviousQuestionUpdatesObserversWhenSelectorDoesNotReturnNull() {
        testGame.goToPreviousQuestion();
        verify(mockObserver).update(eq(testGame), eq(UpdateState.CHANGE_QUESTION));
    }

    @Test
    public void testCorrectAnswerUpdatesObserverWithCorrectAnswerState() {
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(q1);
        testGame.goToNextQuestion();
        testGame.selectAnswer(0);
        verify(mockObserver).update(eq(testGame), eq(UpdateState.CORRECT_ANSWER));
    }

    @Test
    public void testIncorrectAnswerUpdatesObserverWithIncorrectAnswerState() {
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(q1);
        testGame.goToNextQuestion();
        testGame.selectAnswer(1);
        verify(mockObserver).update(eq(testGame), eq(UpdateState.INCORRECT_ANSWER));
    }

    @Test
    public void testGetTotalNumberOfQuestionsDelegatesToQuestionSelector() {
        when(mockQuestionSelector.getTotalNumberOfQuestions()).thenReturn(12);
        assertThat(testGame.getTotalNumberOfQuestions()).isEqualTo(12);
        verify(mockQuestionSelector).getTotalNumberOfQuestions();
    }

}
