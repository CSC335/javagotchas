package com.jermowery.csc335.javagotchas.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

/**
 * Created by Jeremy on 1/1/2016.
 */
public abstract class ApiEnabledActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private boolean mResolvingError;

    protected abstract void setEnabledAllElements(boolean state);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        if (!((ApplicationWithPlayServices) this.getApplicationContext()).isConnected()) {
            this.setEnabledAllElements(false);
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
        if (!((ApplicationWithPlayServices) this.getApplicationContext()).isConnected()) {
            this.setEnabledAllElements(false);
        } else {
            this.setEnabledAllElements(true);
        }
        if (!mResolvingError) {
            ((ApplicationWithPlayServices) this.getApplicationContext()).connect(this, this);
        }
    }

    @Override
    protected void onStop() {
        this.setEnabledAllElements(false);
        ((ApplicationWithPlayServices) this.getApplicationContext()).disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                ((ApplicationWithPlayServices) this.getApplicationContext()).connect(this, this);
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this.getApplicationContext(), "Result code is: " + resultCode, Toast.LENGTH_LONG);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!((ApplicationWithPlayServices) this.getApplicationContext()).isConnecting() &&
                        !((ApplicationWithPlayServices) this.getApplicationContext()).isConnecting()) {
                    ((ApplicationWithPlayServices) this.getApplicationContext()).connect(this, this);
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
    public void onConnected(@Nullable Bundle bundle) {
        this.setEnabledAllElements(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.setEnabledAllElements(false);
        ((ApplicationWithPlayServices) this.getApplicationContext()).reconnect();
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
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
