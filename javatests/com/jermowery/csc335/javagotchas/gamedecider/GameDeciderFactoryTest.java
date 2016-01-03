package com.jermowery.csc335.javagotchas.gamedecider;

import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;
import com.jermowery.csc335.javagotchas.view.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class GameDeciderFactoryTest {

    @Test
    public void testGetTurnsTakenGameDeciderReturnsTurnsTakenGameDecider() {
        assertThat(GameDeciderFactory.getTurnsTakenGameDecider(10)).isInstanceOf(TurnsTakenGameDecider.class);
    }

    @Test
    public void testGetUnendingGameDeciderReturnsUnendingGameDecider() {
        assertThat(GameDeciderFactory.getUnendingGameDecider()).isInstanceOf(UnendingGameDecider.class);
    }

    @Test
    public void testGetGameDeciderReturnsUnendingGamedecider() {
        GameSettings gameSettings = new GameSettings();
        gameSettings.gameDeciderType = GameSettingsProto.UNENDING;
        assertThat(GameDeciderFactory.getGameDecider(gameSettings)).isInstanceOf(UnendingGameDecider.class);
    }

    @Test
    public void testGetGameDeciderReturnsTurnsTakenGamedecider() {
        GameSettings gameSettings = new GameSettings();
        gameSettings.gameDeciderType = GameSettingsProto.TURNS;
        assertThat(GameDeciderFactory.getGameDecider(gameSettings)).isInstanceOf(TurnsTakenGameDecider.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGameDeciderThrowsException() {
        GameSettings gameSettings = new GameSettings();
        gameSettings.gameDeciderType = -1;
        assertThat(GameDeciderFactory.getGameDecider(gameSettings)).isNotNull();
    }

}
