package com.mojodigi.selfiepro.application;

import android.app.Application;
import android.content.Context;

public class SelfieProApplication extends Application {

    private static SelfieProApplication mSelfieProApplication;

    private static final String TAG = SelfieProApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mSelfieProApplication = this;
    }

    public static SelfieProApplication getSelfieProApplication() {
        return mSelfieProApplication;
    }

    public Context getContext() {
        return mSelfieProApplication.getContext();
    }
}
