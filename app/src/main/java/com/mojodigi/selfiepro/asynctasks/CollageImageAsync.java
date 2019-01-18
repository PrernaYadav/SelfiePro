package com.mojodigi.selfiepro.asynctasks;

import android.os.AsyncTask;
import android.widget.ImageView;

public class CollageImageAsync extends AsyncTask<Object, String, String> {
    private ImageView mSquareImage;
    private int mImageId;

    public CollageImageAsync(ImageView squareImage, int image_id) {
        this.mSquareImage = squareImage;
        mImageId = image_id;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mSquareImage.setImageResource(mImageId);
    }

    @Override
    protected String doInBackground(Object... params) {
        return null;
    }

}