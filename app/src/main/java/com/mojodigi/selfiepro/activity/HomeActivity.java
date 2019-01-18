package com.mojodigi.selfiepro.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.collage.CollageSelectedActivity;
import com.mojodigi.selfiepro.utils.Constants;
import com.mojodigi.selfiepro.utils.MyPreference;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private LinearLayout mCameraLayout  , mGalleryLayout , mCollageLayout;
    private static final int PICK_CAMERA_REQUEST = 1;
    private static final int PICK_GALLARY_REQUEST = 2;

    private boolean isKitKat ;

    private MyPreference mMyPrecfence = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(mMyPrecfence == null) {
            mMyPrecfence = MyPreference.getMyPreferenceInstance(HomeActivity.this);
        }
        mMyPrecfence.saveString(Constants.COLLAGE_ACTIVITY , "false");

        mCameraLayout = findViewById(R.id.idCameraLayout);
        mCameraLayout.setOnClickListener(this);

        mGalleryLayout = findViewById(R.id.idGalleryLayout);
        mGalleryLayout.setOnClickListener(this);

        mCollageLayout = findViewById(R.id.idCollageLayout);
        mCollageLayout.setOnClickListener(this);
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.idCameraLayout:
                mMyPrecfence.saveString(Constants.INTENT_TYPE, "IntentCamera");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_CAMERA_REQUEST);

                break;

            case R.id.idGalleryLayout:
                mMyPrecfence.saveString(Constants.INTENT_TYPE, "IntentGallery");
                openGallery();
                break;

            case R.id.idCollageLayout:

                mMyPrecfence.saveString(Constants.INTENT_TYPE, "IntentCollage");
                //Intent collageIntent = new Intent( HomeActivity.this , CollageFramesActivity.class);
                Intent collageIntent = new Intent( HomeActivity.this , CollageSelectedActivity.class);
                startActivity(collageIntent);
                break;
        }
    }


    public void openGallery() {

        if (isKitKat) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_GALLARY_REQUEST);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_GALLARY_REQUEST);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case PICK_CAMERA_REQUEST:
                    Bitmap bitmapPhotoCamera = (Bitmap) data.getExtras().get("data");
                    Intent intentPhotoCamera  = new Intent(HomeActivity.this, EditImageActivity.class);
                    intentPhotoCamera.putExtra("BITMAP_PICK_CAMERA", bitmapPhotoCamera);
                    startActivity(intentPhotoCamera);
                    break;


                case PICK_GALLARY_REQUEST:
                    try {
                        Uri uri = data.getData();
                         Intent intentPhotoGallary  = new Intent(HomeActivity.this, EditImageActivity.class);
                         intentPhotoGallary.putExtra("URI_PICK_GALLARY", uri);
                        startActivity(intentPhotoGallary);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
