package com.jermowery.csc335.javagotchas.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public abstract class ApiEnabledActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private boolean mResolvingError;
    protected GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = new GoogleApiClient.Builder(this.getApplicationContext())
                .addApiIfAvailable(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        this.mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        if (this.getActionBar() != null) {
            this.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.getActionBar().setHomeButtonEnabled(true);
        }
    }

    public void unlockAchievement(String achievementId) {
        boolean signInOptOut = ((ApplicationWithPlayServices)this.getApplicationContext()).signInOptOut();
        if (!signInOptOut && this.client.isConnected()) {
            Games.Achievements.unlock(this.client, achievementId);
        }

    }

    public void incrementAchievement(String achievementId, int amount) {
        boolean signInOptOut = ((ApplicationWithPlayServices)this.getApplicationContext()).signInOptOut();
        if (!signInOptOut && this.client.isConnected()) {
            Games.Achievements.increment(this.client, achievementId, amount);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean signInOptOut = ((ApplicationWithPlayServices) this.getApplicationContext()).signInOptOut();
        if (!mResolvingError && !signInOptOut) {
            this.client.connect();
        }
    }

    @Override
    protected void onStop() {
        this.client.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                this.client.connect();

            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!this.client.isConnected() &&
                        !this.client.isConnecting()) {
                    this.client.connect();
                }
            } else {
                if (this.client.isConnected()) {
                    Games.signOut(this.client);
                    this.client.disconnect();
                }

            }
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onConnectionSuspended(int i) {
        boolean signInOptOut = ((ApplicationWithPlayServices) this.getApplicationContext()).signInOptOut();
        if (!signInOptOut) {
            this.client.reconnect();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((ApiEnabledActivity) getActivity()).onDialogDismissed();
        }
    }


}
