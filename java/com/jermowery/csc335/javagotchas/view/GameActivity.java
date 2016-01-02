package com.jermowery.csc335.javagotchas.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import com.jermowery.csc335.javagotchas.data.DataProvider;
import com.jermowery.csc335.javagotchas.data.DataProviderFactory;
import com.jermowery.csc335.javagotchas.game.Game;
import com.jermowery.csc335.javagotchas.game.GameFactory;
import com.jermowery.csc335.javagotchas.player.PlayerDataProviderFactory;
import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;
import com.jermowery.csc335.javagotchas.proto.nano.PlayerProto.PlayerStats;
import com.jermowery.csc335.javagotchas.proto.nano.PlayerProto.TurnsGameStats;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jeremy on 12/27/2015.
 */
public abstract class GameActivity extends ApiEnabledActivity implements Observer {
    private static final String DATA_FILE_NAME = "data";
    private static final String PLAYER_DATA_FILE = "player";
    protected Game game;
    protected PlayerStats player;
    private AsyncTask<GameSettings, Void, Game> dataTask;

    public PlayerStats getPlayer() {
        return this.player;
    }

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        findViewById(R.id.loadingBar).setVisibility(ProgressBar.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        byte[] playerStats = PlayerStats.toByteArray(this.player);
        try {
            OutputStream os = openFileOutput(PLAYER_DATA_FILE, Context.MODE_PRIVATE);
            os.write(playerStats);
        } catch (IOException ioe) {
            ioe.printStackTrace();
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
                if (Arrays.asList(fileList()).contains(PLAYER_DATA_FILE)) {
                    GameActivity.this.player =
                            PlayerDataProviderFactory.getPlayerDataProvider(openFileInput(PLAYER_DATA_FILE)).getPlayer();
                } else {
                    GameActivity.this.player = new PlayerStats();
                    GameActivity.this.player.turnsGameStats = new TurnsGameStats();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            return GameFactory.getGame(gameSettingses[0], data);
        }

        @Override
        protected void onPostExecute(Game game) {
            super.onPostExecute(game);
            if (GameActivity.this.mGoogleApiClient.isConnected()) {
                findViewById(R.id.loadingBar).setVisibility(ProgressBar.GONE);
            }
        }
    }
}
