package com.jermowery.csc335.javagotchas.game;

import com.jermowery.csc335.javagotchas.BuildConfig;
import com.jermowery.csc335.javagotchas.gamedecider.GameDecider;
import com.jermowery.csc335.javagotchas.gamedecider.TurnsTakenGameDecider;
import com.jermowery.csc335.javagotchas.logic.Score;
import com.jermowery.csc335.javagotchas.logic.UpdateState;
import com.jermowery.csc335.javagotchas.questionselector.InOrderQuestionSelector;
import com.jermowery.csc335.javagotchas.questionselector.QuestionSelector;
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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Answer;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;

import java.util.Observer;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by jeremy on 12/21/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class GameTest {
    private Question q1;
    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private QuestionSelector mockQuestionSelector;
    @Mock private GameDecider mockGameDecider;
    @Mock private Score mockScore;
    @Mock private Observer mockObserver;
    @InjectMocks private Game testGame;

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

        when(mockQuestionSelector.moveToNextQuestion()).thenReturn(mockQuestionSelector);
        when(mockQuestionSelector.moveToPreviousQuestion()).thenReturn(mockQuestionSelector);
        when(mockQuestionSelector.getCurrentQuestion()).thenReturn(Question.getDefaultInstance());
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
    public void testGetNumTurnsTakenIncrementsOnGoToNextQuestion() {
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(0);
        testGame.goToNextQuestion();
        assertThat(testGame.getNumTurnsTaken()).isEqualTo(1);
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
        verify(mockQuestionSelector, times(2)).moveToPreviousQuestion();
        verify(mockQuestionSelector, times(2)).getCurrentQuestion();
        testGame.goToPreviousQuestion();
        verify(mockQuestionSelector, times(4)).moveToPreviousQuestion();
        verify(mockQuestionSelector, times(3)).getCurrentQuestion();
    }

    @Test
    public void testGetCurrentQuestionInitial() {
        // Move to next question first because at construction the mock value is not used
        testGame.goToNextQuestion();
        assertThat(testGame.getCurrentQuestion()).isEqualTo(Question.getDefaultInstance());
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
        ArgumentMatcher<Integer> isPositive = new ArgumentMatcher() {
            @Override
            public boolean matches(Object argument) {
                return (int)argument > 0;
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
        verify(mockObserver, never()).update(anyObject(), anyObject());
    }

    @Test
    public void testGoToPreviousQuestionDoesNotUpdateObserversWhenSelectorReturnsNull() {
        when(mockQuestionSelector.moveToPreviousQuestion()).thenReturn(null);
        testGame.goToPreviousQuestion();
        verify(mockObserver, never()).update(anyObject(), anyObject());
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

}