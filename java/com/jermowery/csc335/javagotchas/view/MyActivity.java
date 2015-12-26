package com.jermowery.csc335.javagotchas.view;

import android.app.Activity;
import android.os.Bundle;
import com.jermowery.csc335.javagotchas.R;
import com.jermowery.csc335.javagotchas.data.DataProvider;
import com.jermowery.csc335.javagotchas.data.DataProviderFactory;
import com.jermowery.csc335.javagotchas.game.Game;
import com.jermowery.csc335.javagotchas.gamedecider.TurnsTakenGameDecider;
import com.jermowery.csc335.javagotchas.logic.Score;
import com.jermowery.csc335.javagotchas.questionselector.QuestionSelectorFactory;

import java.io.IOException;
import java.util.Random;

public class MyActivity extends Activity {
    private static final int MAX_TURNS = 10;
    private static final int MAX_SCORE = 10;
    private Game game;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DataProvider dp = null;
        try {
            dp = DataProviderFactory.getDefaultDataProvider(getAssets().open("data"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
        this.game = new Game(
                QuestionSelectorFactory.getRandomQuestionSelector(dp.getData(), new Random()),
                new Score(MAX_SCORE, 0),
                new TurnsTakenGameDecider(MAX_TURNS)
        );
    }
}
