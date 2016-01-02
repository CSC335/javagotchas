package com.jermowery.csc335.javagotchas.questionselector;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.view.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by Jeremy on 12/30/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class QuestionSelectorTest {

    @Test
    public void testGetTotalNumberOfQuestions() {
        Data data = new Data();
        data.question = new Question[5];
        QuestionSelector testSelector = new QuestionSelector(data) {
            @Override
            public QuestionSelector moveToNextQuestion() {
                return null;
            }

            @Override
            public QuestionSelector moveToPreviousQuestion() {
                return null;
            }

            @Override
            public Question getCurrentQuestion() {
                return null;
            }

            @Override
            public QuestionSelector goToQuestion(int index) {
                return null;
            }
        };
        assertThat(testSelector.getTotalNumberOfQuestions()).isEqualTo(5);

    }
}
