package com.jermowery.csc335.javagotchas.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jermowery.csc335.javagotchas.R;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;

public class MenuActivity extends Activity {
    private static final int MAX_TURNS = 10;
    private static final int MAX_SCORE = 10;
    private GameSettings gameSettings;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button startGameButton = (Button) findViewById(R.id.startGameButton);
        Button viewQuestionsButton = (Button) findViewById(R.id.viewQuestionsButton);
        this.gameSettings = new GameSettings();

    }

    public void onStartGameButtonClick(View view) {
        MenuActivity.this.gameSettings.questionSelectorType = GameSettingsProto.RANDOM;
        MenuActivity.this.gameSettings.gameDeciderType = GameSettingsProto.TURNS;
        startGameActivity(TurnsGameActivity.class);
    }

    public void onViewQuestionButtonClick(View view) {
        MenuActivity.this.gameSettings.questionSelectorType = GameSettingsProto.IN_ORDER;
        MenuActivity.this.gameSettings.gameDeciderType = GameSettingsProto.UNENDING;
        startGameActivity(ViewGameActivity.class);
    }

    public void startGameActivity(Class<? extends GameActivity> activity) {
        Intent gameActivity = new Intent(getApplicationContext(), activity);
        gameActivity.putExtra(getString(R.string.game_decider_type), this.gameSettings.gameDeciderType);
        gameActivity.putExtra(getString(R.string.question_selector_type), this.gameSettings.questionSelectorType);
        startActivity(gameActivity);
    }
}
