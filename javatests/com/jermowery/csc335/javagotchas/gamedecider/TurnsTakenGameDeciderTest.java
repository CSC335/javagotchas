package com.jermowery.csc335.javagotchas.gamedecider;

import com.jermowery.csc335.javagotchas.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by jeremy on 12/21/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class TurnsTakenGameDeciderTest {
    private GameDecider testDecider;

    @Before
    public void setUp() {
        this.testDecider = new TurnsTakenGameDecider(10);
    }

    @Test
    public void testNegativeTurns() {
        assertThat(this.testDecider.isOver(-10)).isFalse();
    }

    @Test
    public void testZeroTurns() {
        assertThat(this.testDecider.isOver(0)).isFalse();
    }

    @Test
    public void testPositiveUnderMaxTurns() {
        assertThat(this.testDecider.isOver(9)).isFalse();
    }

    @Test
    public void testPositiveMaxTurns() {
        assertThat(this.testDecider.isOver(10)).isTrue();
    }

    @Test
    public void testPositiveOverMaxTurns() {
        assertThat(this.testDecider.isOver(11)).isTrue();
    }
}
