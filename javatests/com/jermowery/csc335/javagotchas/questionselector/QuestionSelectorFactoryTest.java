package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.BuildConfig;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
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


}
