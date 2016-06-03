package org.jcloarca.visitguatemala;

import android.app.Application;

/**
 * Created by JCLoarca on 6/3/2016.
 */
public class VisitGuatemalaApp extends Application {
    private final static String URL_ABOUT = "https://about.me/joseloarca";

    public static String getUrlAbout() {
        return URL_ABOUT;
    }
}
