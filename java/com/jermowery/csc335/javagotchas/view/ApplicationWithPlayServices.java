package com.jermowery.csc335.javagotchas.view;

import android.app.Activity;
import android.app.Application;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;

/**
 * Created by Jeremy on 1/1/2016.
 */
public class ApplicationWithPlayServices extends Application {

    private static final int ACHIEVEMENTS_ACTIVITY = 42;
    private GoogleApiClient client;
    private ConnectionCallbacks currentCallbacks;
    private OnConnectionFailedListener currentListener;
    private int numConnections;

    @Override
    public void onCreate() {
        super.onCreate();
        this.client = new GoogleApiClient.Builder(this.getApplicationContext())
                .addApiIfAvailable(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
        this.numConnections = 0;
    }

    public GoogleApiClient getClient(ConnectionCallbacks callbacks, OnConnectionFailedListener listener) {
        if (this.currentCallbacks != null) {
            this.client.unregisterConnectionCallbacks(this.currentCallbacks);
        }
        if (this.currentListener != null) {
            this.client.unregisterConnectionFailedListener(this.currentListener);
        }

        this.currentCallbacks = callbacks;
        this.currentListener = listener;

        this.client.registerConnectionCallbacks(this.currentCallbacks);
        this.client.registerConnectionFailedListener(this.currentListener);

        return this.client;
    }

    public void connect(ConnectionCallbacks callbacks, OnConnectionFailedListener listener) {
        synchronized (this) {
            this.numConnections++;
            this.registerNewConnection(callbacks, listener);
            this.client.connect();
        }
    }

    public void disconnect() {
        synchronized (this) {
            this.numConnections--;
            if (this.numConnections == 0) {
                this.client.disconnect();
            }
        }
    }

    public void unlockAchievement(String achievementId) {
        Games.Achievements.unlock(this.client, achievementId);
    }

    public void incrementAchievement(String achievementId, int amount) {
        Games.Achievements.increment(this.client, achievementId, amount);
    }

    private void registerNewConnection(ConnectionCallbacks callbacks, OnConnectionFailedListener listener) {
        if (this.currentCallbacks != null) {
            this.client.unregisterConnectionCallbacks(this.currentCallbacks);
        }
        if (this.currentListener != null) {
            this.client.unregisterConnectionFailedListener(this.currentListener);
        }

        this.currentCallbacks = callbacks;
        this.currentListener = listener;

        this.client.registerConnectionCallbacks(this.currentCallbacks);
        this.client.registerConnectionFailedListener(this.currentListener);
    }

    public boolean isConnected() {
        synchronized (this) {
            return this.client.isConnected();
        }
    }

    public boolean isConnecting() {
        synchronized (this) {
            return this.client.isConnecting();
        }
    }

    public void reconnect() {
        synchronized (this) {
            this.client.reconnect();
        }
    }

    public void startAchievementsActivity(Activity startingActivity) {
        startingActivity.startActivityForResult(Games.Achievements.getAchievementsIntent(this.client), ACHIEVEMENTS_ACTIVITY);
    }
}
