package com.jermowery.csc335.javagotchas.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class MenuActivity extends ApiEnabledActivity {
    private static final int MAX_TURNS = 10;
    private static final int MAX_SCORE = 10;
    private Button startGameButton;
    private Button viewQuestionsButton;
    private Button achievementsButton;
    private GameSettings gameSettings;
    private DrawerLayout navigationDrawer;
    private ListView navigationDrawerItems;
    private ActionBarDrawerToggle toggle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.main);
        this.startGameButton = (Button) findViewById(R.id.startGameButton);
        this.viewQuestionsButton = (Button) findViewById(R.id.viewQuestionsButton);
        this.achievementsButton = (Button) findViewById(R.id.achievementsButton);
        this.navigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        this.navigationDrawerItems = (ListView) findViewById(R.id.navigation_drawer_items);
        String[] items = new String[1];
        items[0] = ((ApplicationWithPlayServices) MenuActivity.this.getApplicationContext()).getSignedOut() ?
                getString(R.string.sign_in) : getString(R.string.sign_out);
        navigationDrawerItems.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, items));
        navigationDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private ApplicationWithPlayServices application;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                application = (ApplicationWithPlayServices) MenuActivity.this.getApplicationContext();
                String[] items = new String[1];
                setEnabledAllElements(false);
                if (application.getSignedOut()) {
                    application.signIn(MenuActivity.this, MenuActivity.this);
                    items[0] = getString(R.string.sign_out);
                } else {
                    application.signOut();
                    items[0] = getString(R.string.sign_in);
                }
                navigationDrawerItems.setAdapter(new ArrayAdapter<>(
                        MenuActivity.this, R.layout.drawer_list_item, items));

                MenuActivity.this.navigationDrawer.closeDrawer(MenuActivity.this.navigationDrawerItems);

            }
        });
        this.toggle = new ActionBarDrawerToggle(
                this, this.navigationDrawer, R.string.open_drawer, R.string.close_drawer);
        this.navigationDrawer.setDrawerListener(this.toggle);
        super.onCreate(savedInstanceState);
        this.gameSettings = new GameSettings();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (this.toggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && requestCode != ApplicationWithPlayServices.ACHIEVEMENTS_ACTIVITY) {
            navigationDrawerItems.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, new String[]{
                    getString(R.string.sign_in)}));
        }
    }

    @Override
    protected void setEnabledAllElements(boolean state) {
        this.startGameButton.setEnabled(state);
        this.viewQuestionsButton.setEnabled(state);
        this.achievementsButton.setEnabled(state);
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.setEnabledAllElements(false);
        super.onConnectionSuspended(i);
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

    public void onAchievementsButtonClick(View view) {
        ((ApplicationWithPlayServices) this.getApplicationContext()).startAchievementsActivity(this);
    }
}
