package com.jermowery.csc335.javagotchas.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.jermowery.csc335.javagotchas.R;
import com.jermowery.csc335.javagotchas.data.DataProvider;
import com.jermowery.csc335.javagotchas.data.DataProviderFactory;
import com.jermowery.csc335.javagotchas.game.Game;
import com.jermowery.csc335.javagotchas.game.GameFactory;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;

import java.io.IOException;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jeremy on 12/27/2015.
 */
public abstract class GameActivity extends Activity implements Observer {
    private static final String DATA_FILE_NAME = "data";
    protected Game game;
    private AsyncTask<GameSettings, Void, Game> dataTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Intent receivingIntent = this.getIntent();
        if (!receivingIntent.hasExtra(getString(R.string.game_decider_type))
                || !receivingIntent.hasExtra(getString(R.string.question_selector_type))) {
            throw new IllegalArgumentException(
                    "Intemt started without necessary game decider type and question selector type");
        }
        GameSettings gameSettings = new GameSettings();
        gameSettings.gameDeciderType = receivingIntent.getIntExtra(getString(R.string.game_decider_type), 1);
        gameSettings.questionSelectorType = receivingIntent.getIntExtra(getString(R.string.question_selector_type), 1);
        this.dataTask = new DataOperation();
        this.dataTask.execute(gameSettings);
        try {
            this.game = this.dataTask.get();
            this.game.addObserver(this);
            this.startGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    protected abstract void startGame();

    private class DataOperation extends AsyncTask<GameSettings, Void, Game> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loadingBar).setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Game doInBackground(GameSettings... gameSettingses) {
            Data data = null;

            try {
                DataProvider dataProvider = DataProviderFactory.getDataProvider(getAssets().open(DATA_FILE_NAME));
                data = dataProvider.getData();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            return GameFactory.getGame(gameSettingses[0], data);
        }

        @Override
        protected void onPostExecute(Game game) {
            super.onPostExecute(game);
            findViewById(R.id.loadingBar).setVisibility(ProgressBar.GONE);
        }
    }
}
