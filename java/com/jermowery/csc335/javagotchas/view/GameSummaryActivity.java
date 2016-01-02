package com.jermowery.csc335.javagotchas.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jermowery.csc335.javagotchas.logic.Score;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;

/**
 * Created by Jeremy on 12/30/2015.
 */
public class GameSummaryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        if (!this.getIntent().hasExtra(getString(R.string.score))) {
            throw new IllegalArgumentException("Summary called without score.");
        }
        Score score = (Score) this.getIntent().getSerializableExtra(getString(R.string.score));
        TextView gameSummary = (TextView) findViewById(R.id.summaryText);
        gameSummary.setText("Summary:\n\n\tScore: " + score.getCurrentScore() + "\\" + score.getMaxScore());
        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameActivity = new Intent(getApplicationContext(), TurnsGameActivity.class);
                gameActivity.putExtra(getString(R.string.game_decider_type), GameSettingsProto.RANDOM);
                gameActivity.putExtra(getString(R.string.question_selector_type), GameSettingsProto.TURNS);
                startActivity(gameActivity);
                finish();
            }
        });
        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(menuActivity);
                finish();
            }
        });
    }
}
