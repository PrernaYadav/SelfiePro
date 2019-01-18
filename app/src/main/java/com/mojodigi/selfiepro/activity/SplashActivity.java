package com.mojodigi.selfiepro.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.permission.PermissionsActivity;
import com.mojodigi.selfiepro.utils.MyPreference;
import com.mojodigi.selfiepro.utils.Utilities;



public class SplashActivity extends PermissionsActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS = 20;

    private static int SPLASH_TIME_OUT = 2000;

    private LinearLayout mSplashLayout;
    private MyPreference myPreference = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(myPreference==null) {
            myPreference = MyPreference.getMyPreferenceInstance(SplashActivity.this);
        }

        mSplashLayout = (LinearLayout)findViewById(R.id.idSplashLayout);

        if (!checkPermission()) {
            SplashActivity.super.requestAppPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, R.string
                    .runtime_permissions_txt  , REQUEST_PERMISSIONS);
        } else {
            callHandler();
        }
    }



    private boolean checkPermission() {
        boolean permissionCheckStatus;

        int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        int permissionCheckWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permissionCheckCamera > -1) {
            if (permissionCheckWrite > -1) {
                permissionCheckStatus = true;
            } else {
                permissionCheckStatus = false;
            }
        } else {
            permissionCheckStatus = false;
        }
        return permissionCheckStatus;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Utilities.getUtilInstance(SplashActivity.this).showSnackBarLong("Permissions has been Received." ,  mSplashLayout);
        callHandler();
    }


    private void callHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Utilities.isNetworkConnected(SplashActivity.this)) {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    SplashActivity.this.finish();
                     }
                    else {
                    Log.e(TAG, "Please check your internet connection and try again.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, SPLASH_TIME_OUT);
    }

}
