package com.jermowery.csc335.javagotchas.view;

import android.app.Application;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class ApplicationWithPlayServices extends Application {

    private static boolean signInOptOut = false;

    public static boolean signInOptOut() {
        return signInOptOut;
    }

    public static void setSignInOptOut(boolean state) {
        signInOptOut = state;
    }
}
