package com.jermowery.csc335.javagotchas.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.games.Games;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto;
import com.jermowery.csc335.javagotchas.proto.nano.GameSettingsProto.GameSettings;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class MenuActivity extends ApiEnabledActivity {
    public static final int ACHIEVEMENTS_ACTIVITY = 42;
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
        this.achievementsButton = (Button) findViewById(R.id.achievementsButton);
        this.navigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        this.navigationDrawerItems = (ListView) findViewById(R.id.navigation_drawer_items);
        String[] items = new String[1];
        items[0] = ((ApplicationWithPlayServices) MenuActivity.this.getApplicationContext()).signInOptOut() ?
                getString(R.string.sign_in) : getString(R.string.sign_out);
        navigationDrawerItems.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, items));
        navigationDrawerItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ApplicationWithPlayServices application =
                        (ApplicationWithPlayServices) MenuActivity.this.getApplicationContext();
                String[] items = new String[1];
                if (application.signInOptOut()) {
                    MenuActivity.this.client.connect();
                    items[0] = getString(R.string.sign_out);
                } else {
                    MenuActivity.this.setAchievementsButtonEnabled(false);
                    application.setSignInOptOut(true);
                    Games.signOut(MenuActivity.this.client);
                    MenuActivity.this.client.disconnect();
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
    public void onConnected(@Nullable Bundle bundle) {
        ((ApplicationWithPlayServices) MenuActivity.this.getApplicationContext()).setSignInOptOut(false);
        this.setAchievementsButtonEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.setAchievementsButtonEnabled(false);
        super.onConnectionSuspended(i);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && requestCode != ACHIEVEMENTS_ACTIVITY) {
            navigationDrawerItems.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, new String[]{
                    getString(R.string.sign_in)}));
        }
    }

    private void setAchievementsButtonEnabled(boolean state) {
        this.achievementsButton.setEnabled(state);
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
        this.startActivityForResult(Games.Achievements.getAchievementsIntent(this.client), ACHIEVEMENTS_ACTIVITY);
    }
}
