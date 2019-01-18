package com.mojodigi.selfiepro.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.mojodigi.selfiepro.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities {

    private Context mContext;
    private static Utilities mUtilInstance = null;

    private Utilities() {

    }

    private Utilities(Context context) {
        this.mContext = context;
    }

    public static Utilities getUtilInstance(Context context) {

        if (mUtilInstance == null) {
            mUtilInstance = new Utilities(context);
        }
        return mUtilInstance;
    }




    public static String getStringFromXml(Context mContext,int resId)
    {
        return mContext.getResources().getString(resId);
    }

    private void redirectToactivity(Class targetClass)
    {
        Intent i = new Intent(mContext, targetClass);
        mContext.startActivity(i);
    }




    public double parseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return -1;
            }
        }
        else return 0;
    }


    public void showSnackBarLong(String message, View view) {
        if (view != null && !TextUtils.isEmpty(message)) {
            Snackbar mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            mSnackbar.setActionTextColor(Color.blue(R.color.white));
            mSnackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.black));
            mSnackbar.show();
        }
    }


    public void showSnackBarShort(String message, View view) {
        if (view != null && !TextUtils.isEmpty(message)) {
            Snackbar mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            mSnackbar.setActionTextColor(Color.blue(R.color.white));
            mSnackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.black));
            mSnackbar.show();

        }
    }


    public void showToastShort(Context context, String message) {
        if (context != null && !TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToastLong(Context context, String message) {
        if (context != null && !TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

//    public void openWebViewActivity(String url) {
//
//        Intent intent = new Intent(context, WebViewActivity.class);
//        intent.putExtra(WebViewActivity.URL, url);
//        context.startActivity(intent);
//    }

//    public void startActivity(Class<?> cls, boolean isFinish) {
//
//        Intent intent = new Intent(context, cls);
//        context.startActivity(intent);
//
//        if (isFinish) {
//            ((BaseActivity) context).finish();
//        }
//    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWiFiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            return true;

        } else {
            return false;
        }
    }




//    public static ProgressDialog generateProgressDialog(Context context, boolean cancelable) {
//        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressTheme);
//        progressDialog.setMessage(context.getString(R.string.Loading));
//        progressDialog.setCancelable(cancelable);
//        return progressDialog;
//    }




    public static int dpToPx(Context context, int dp) {
        return Math.round(dp * getPixelScaleFactor(context));
    }

    public static int pxToDp(Context context, int px) {
        return Math.round(px / getPixelScaleFactor(context));
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();
        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ? drawable.getBounds().width() : drawable.getIntrinsicWidth();
        final int height = !drawable.getBounds().isEmpty() ? drawable.getBounds().height() : drawable.getIntrinsicHeight();
        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static boolean isEmpty(String target) {
        if (target.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSame(String target, String newtar) {
        if (target.equals(newtar)) {
            return true;
        } else {
            return false;
        }
    }



    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
