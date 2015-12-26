package com.jermowery.csc335.javagotchas;

import android.app.Activity;
import android.os.Bundle;
import com.jermowery.csc335.javagotchas.data.DataProvider;
import com.jermowery.csc335.javagotchas.data.DataProviderFactory;
import com.jermowery.csc335.javagotchas.game.Game;
import com.jermowery.csc335.javagotchas.gamedecider.TurnsTakenGameDecider;
import com.jermowery.csc335.javagotchas.logic.Score;
import com.jermowery.csc335.javagotchas.questionselector.QuestionSelectorFactory;

import java.io.FileNotFoundException;
import java.util.Random;

public class MyActivity extends Activity {
    private Game game;
    private static final int MAX_TURNS = 10;
    private static final int MAX_SCORE = 10;

    public MyActivity() {
        DataProvider dp = null;
        try {
            dp = DataProviderFactory.getDefaultDataProvider(openFileInput("gamedata"));
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
            System.exit(1);
        }
        this.game = new Game(
                QuestionSelectorFactory.getRandomQuestionSelector(dp.getData(), new Random()),
                new Score(MAX_SCORE, 0),
                new TurnsTakenGameDecider(MAX_TURNS)
        );
    }
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
