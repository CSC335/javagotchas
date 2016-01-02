package com.jermowery.csc335.javagotchas.gamedecider;

import com.jermowery.csc335.javagotchas.view.BuildConfig;
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
public class UnendingGameDeciderTest {

    private GameDecider testDecider;

    @Before
    public void setUp() {
        this.testDecider = new UnendingGameDecider();
    }

    @Test
    public void testNegativeTurns() {
        assertThat(this.testDecider.isOver(-1000)).isFalse();
    }

    @Test
    public void testPositiveTurns() {
        assertThat(this.testDecider.isOver(1000)).isFalse();
    }

    @Test
    public void testZeroTurns() {
        assertThat(this.testDecider.isOver(0)).isFalse();
    }
}
