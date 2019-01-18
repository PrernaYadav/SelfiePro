package com.mojodigi.selfiepro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



public class MyPreference {

    public static Context mContext;
    private static MyPreference myPrecfenceInstance = null;
    private SharedPreferences mSharedPreferences;
    private int MODE_PRIVATE = 0;
    private SharedPreferences.Editor mEditor;


    public MyPreference(Context context) {
        this.mContext = context;
        mSharedPreferences = this.mContext.getSharedPreferences("JMM_APP", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }


    public static MyPreference getMyPreferenceInstance(Context context) {
        if (myPrecfenceInstance == null) {
            myPrecfenceInstance = new MyPreference(context);
        }
        return myPrecfenceInstance;
    }


    public void saveString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }


    public String getString(String key) {
        String result = "";
        result = mSharedPreferences.getString(key, "");
        return result;
    }



    public void saveCountString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }


    public String getCountString(String key) {
        String result = "";
        result = mSharedPreferences.getString(key, "0");
        return result;
    }

    public void saveInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public int getInt(String key) {
        return  mSharedPreferences.getInt(key, 0);
    }


    public void saveBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }


    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }


    public void saveLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }


    public boolean clearKeyData(String key) {
        try {
            mEditor.remove(key);
            mEditor.commit();
        } catch (Exception ex) {
            Log.e("Clear Shared Preference", "Exception");
        }
        return true;
    }



    public boolean clearSharedPreference() {
        try {
            mEditor.clear();
            mEditor.commit();
        } catch (Exception ex) {
            Log.e("Clear Shared Preference", "Exception");
        }
        return true;
    }
}
