package com.jermowery.csc335.javagotchas.player;

import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.jermowery.csc335.javagotchas.proto.nano.PlayerProto.PlayerStats;
import com.jermowery.csc335.javagotchas.proto.nano.PlayerProto.TurnsGameStats;
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
 * Created by Jeremy on 1/3/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class PlayerDataProviderFactoryTest {

    @Test
    public void testGetPlayerDataProviderIsFromProtoDataProvider() throws IOException {
        PlayerStats stats = new PlayerStats();
        stats.turnsGameStats = new TurnsGameStats();
        stats.turnsGameStats.questionsCorrect = 5;
        stats.turnsGameStats.questionsIncorrect = 7;
        stats.turnsGameStats.questionsAttempted = 12;
        stats.turnsGameStats.currentCorrectStreak = 0;
        stats.turnsGameStats.currentIncorrectStreak = 5;
        stats.turnsGameStats.maxCorrectStreak = 5;
        stats.turnsGameStats.maxIncorrectStreak = 1;
        byte[] ba = new byte[14];
        stats.writeTo(CodedOutputByteBufferNano.newInstance(ba));
        InputStream is = new ByteArrayInputStream(ba);
        PlayerDataProvider actual = PlayerDataProviderFactory.getPlayerDataProvider(is);
        assertThat(actual).isInstanceOf(FromProtoPlayerDataProvider.class);
    }
}
