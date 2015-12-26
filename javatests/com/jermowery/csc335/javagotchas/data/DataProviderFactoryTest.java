package com.jermowery.csc335.javagotchas.data;

import android.os.Build;
import com.jermowery.csc335.javagotchas.BuildConfig;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Answer;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by jeremy on 12/18/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class DataProviderFactoryTest {
    @Test
    public void testGetDefaultDataProviderBehavesLikeProtoProvider() throws IOException {
        Data data = Data.newBuilder()
                .addQuestion(
                        Question.newBuilder()
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
                                )
                )
                .addQuestion(
                        Question.newBuilder()
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
                                )
                )
                .build();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        data.writeTo(os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Data actual = new FromProtoDataProvider(is).getData();
        assertThat(actual).isEqualTo(data);
        is = new ByteArrayInputStream(new byte[1]);
        actual = DataProviderFactory.getDefaultDataProvider(is).getData();
        assertThat(actual).isNull();
    }

}
