package com.jermowery.csc335.javagotchas.data;

import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Answer;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Question;
import com.jermowery.csc335.javagotchas.view.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayInputStream;
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
        Data data = new Data();
        data.question = new Question[2];
        Question q1 = new Question();
        q1.id = 0;
        q1.text = "Question 0";
        q1.explanation = "Explanation 0";
        q1.answer = new Answer[2];
        q1.answer[0] = new Answer();
        q1.answer[0].isCorrect = true;
        q1.answer[0].text = "Answer 1";
        q1.answer[1] = new Answer();
        q1.answer[1].isCorrect = false;
        q1.answer[1].text = "Answer 2";
        data.question[0] = q1;
        Question q2 = new Question();
        q2.id = 1;
        q2.text = "Question 1";
        q2.explanation = "Explanation 1";
        q2.answer = new Answer[2];
        q2.answer[0] = new Answer();
        q2.answer[0].isCorrect = false;
        q2.answer[0].text = "Answer 1";
        q2.answer[1] = new Answer();
        q2.answer[1].isCorrect = true;
        q2.answer[1].text = "Answer 2";
        data.question[1] = q2;

        byte[] ba = new byte[112];
        data.writeTo(CodedOutputByteBufferNano.newInstance(ba));
        InputStream is = new ByteArrayInputStream(ba);
        Data actual = new FromProtoDataProvider(is).getData();
        assertThat(actual.toString()).isEqualTo(data.toString());
        is = new ByteArrayInputStream(new byte[1]);
        actual = DataProviderFactory.getDataProvider(is).getData();
        assertThat(actual).isNull();
    }

}
