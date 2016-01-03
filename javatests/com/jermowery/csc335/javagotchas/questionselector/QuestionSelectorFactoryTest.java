package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;
import com.jermowery.csc335.javagotchas.view.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class QuestionSelectorFactoryTest {
    @Test
    public void testGetRandomQuestionSelectorReturnsRandomQuestionSelector() {
        Data data = new Data();
        data.question = new Question[1];
        data.question[0] = new Question();
        assertThat(QuestionSelectorFactory.getRandomQuestionSelector(data, new Random()))
                .isInstanceOf(RandomQuestionSelector.class);
    }

    @Test
    public void testGetInOrderQuestionSelectorReturnsInOrderQuestionSelector() {
        assertThat(QuestionSelectorFactory.getInOrderQuestionSelector(new Data()))
                .isInstanceOf(InOrderQuestionSelector.class);
    }

    @Test
    public void testGetQuestionSelectorReturnsInOrderQuestionSelector() {
        GameSettings gameSettings = new GameSettings();
        gameSettings.questionSelectorType = GameSettingsProto.IN_ORDER;
        assertThat(QuestionSelectorFactory.getQuestionSelector(gameSettings, new Data()))
                .isInstanceOf(InOrderQuestionSelector.class);
    }

    @Test
    public void testGetQuestionSelectorReturnsRandomSelector() {
        GameSettings gameSettings = new GameSettings();
        gameSettings.questionSelectorType = GameSettingsProto.RANDOM;
        Data data = new Data();
        data.question = new Question[1];
        data.question[0] = new Question();
        assertThat(QuestionSelectorFactory.getQuestionSelector(gameSettings, data))
                .isInstanceOf(RandomQuestionSelector.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetQuestionSelectorThrowsException() {
        GameSettings gameSettings = new GameSettings();
        gameSettings.questionSelectorType = -1;
        assertThat(QuestionSelectorFactory.getQuestionSelector(gameSettings, new Data()))
                .isNotNull();
    }
}
