package com.jermowery.csc335.javagotchas.view;

import android.app.Application;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;

/**
 * Created by Jeremy on 1/1/2016.
 */
public class ApplicationWithPlayServices extends Application {

    private GoogleApiClient client;
    private ConnectionCallbacks currentCallbacks;
    private OnConnectionFailedListener currentListener;

    @Override
    public void onCreate() {
        super.onCreate();
        this.client = new GoogleApiClient.Builder(this.getApplicationContext())
                .addApiIfAvailable(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
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
}
