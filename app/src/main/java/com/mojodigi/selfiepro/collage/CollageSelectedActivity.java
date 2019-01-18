package com.mojodigi.selfiepro.collage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.activity.EditImageActivity;
import com.mojodigi.selfiepro.adapter.CollageEditToolsAdapter;
import com.mojodigi.selfiepro.adapter.CollageRecycleAdapter;
import com.mojodigi.selfiepro.enums.CollageEditToolsType;
import com.mojodigi.selfiepro.enums.CollageFrameType;
import com.mojodigi.selfiepro.interfaces.OnCollageEditTollsSelected;
import com.mojodigi.selfiepro.interfaces.OnCollageFrameSelected;
import com.mojodigi.selfiepro.utils.Constants;
import com.mojodigi.selfiepro.utils.MyPreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;



public class CollageSelectedActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener,
        OnCollageFrameSelected, OnCollageEditTollsSelected {

    private static final String TAG = CollageSelectedActivity.class.getSimpleName();


    private static final int PICK_CAMERA_REQUEST_ONE = 11;
    private static final int PICK_CAMERA_REQUEST_TWO = 22;

    private static final int PICK_GALLARY_REQUEST_ONE = 111;
    private static final int PICK_GALLARY_REQUEST_TWO = 222;
    private static final int PICK_GALLARY_REQUEST_THREE = 333;
    private static final int PICK_GALLARY_REQUEST_FOUR = 444;
    private static final int PICK_GALLARY_REQUEST_FIVE = 555;
    private static final int PICK_GALLARY_REQUEST_SIX = 666;

    // These matrices will be used to move and zoom image
    private Matrix  mMatrixShape1 , mMatrixShape2 , mMatrixShape3 , mMatrixShape4 , mMatrixShape5 , mMatrixShape6 ;
    private Matrix  mMatrixShape2_1 , mMatrixShape2_2;

    private Matrix  mMatrixSaved1 , mMatrixSaved2 , mMatrixSaved3 , mMatrixSaved4 , mMatrixSaved5 , mMatrixSaved6;
    private Matrix  mMatrixSaved2_1 , mMatrixSaved2_2;

    // We can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    // Remember some things for zooming
    private PointF startPointF, midPointF;
    private float oldDist = 1f;

    private Bitmap mBitmap , mBitmap1 , mBitmap2 , mBitmap3 , mBitmap4 , mBitmap5 , mBitmap6 ;
    private Bitmap mBitmapRotate1, mBitmapRotate2, mBitmapRotate3 , mBitmapRotate4 , mBitmapRotate5 , mBitmapRotate6;

    private ImageView mCollageFrameShape1, mCollageFrameShape2, mCollageFrameShape3, mCollageFrameShape4 , mCollageFrameShape5 ,mCollageFrameShape6, mMainCollageFrameImage;


    private boolean isSelectedImage = true;
    private boolean isSelectedImage1 = true;
    private boolean isSelectedImage2 = true;
    private boolean isSelectedImage3 = true;
    private boolean isSelectedImage4 = true;
    private boolean isSelectedImage5 = true;
    private boolean isSelectedImage6 = true;
    private String mImageName;

    private RelativeLayout mCollageRLayout;

    private boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    private LinearLayout mCollageEditToolsCloseLLayout, mCollageEditBackLLayout, mCollageEditNextLLayout;

    private MyPreference mMyPrecfence = null;


    private RecyclerView mCollageListRecycleView;
    private RecyclerView mCollageEditToolsRecycleView;


    private CollageRecycleAdapter mCollageRecycleAdapter;
    private CollageEditToolsAdapter mCollageEditToolsAdapter;
    private String mIntentType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collage_selected_image);


        if (mMyPrecfence == null) {
            mMyPrecfence = MyPreference.getMyPreferenceInstance(CollageSelectedActivity.this);
        }

        try {
            mMyPrecfence.saveString(Constants.COLLAGE_ACTIVITY , "true");
            mIntentType = mMyPrecfence.getString(Constants.INTENT_TYPE);
        } catch (Exception ex) {
            ex.getStackTrace();
        }



        mMainCollageFrameImage = (ImageView) findViewById(R.id.idMainCollageFrameImage);
        if (mIntentType.equalsIgnoreCase("IntentCollage")) {
            mMainCollageFrameImage.setBackgroundResource(R.drawable.collage2_1);
        }


        initView();
    }

    private void initView() {

        mMatrixShape2_1 = new Matrix();
        mMatrixShape2_2 = new Matrix();

        mMatrixShape1 = new Matrix();
        mMatrixShape2 = new Matrix();
        mMatrixShape3 = new Matrix();
        mMatrixShape4 = new Matrix();
        mMatrixShape5 = new Matrix();
        mMatrixShape6 = new Matrix();

        mMatrixSaved2_1 = new Matrix();
        mMatrixSaved2_2 = new Matrix();

        mMatrixSaved1 = new Matrix();
        mMatrixSaved2 = new Matrix();
        mMatrixSaved3 = new Matrix();
        mMatrixSaved4 = new Matrix();
        mMatrixSaved5 = new Matrix();
        mMatrixSaved6 = new Matrix();

        startPointF = new PointF();
        midPointF = new PointF();

        mCollageRecycleAdapter = new CollageRecycleAdapter(this);
        mCollageEditToolsAdapter = new CollageEditToolsAdapter(this);

        mCollageRLayout = (RelativeLayout) findViewById(R.id.idCollageRLayout);
        mMainCollageFrameImage = (ImageView) findViewById(R.id.idMainCollageFrameImage);
        mCollageFrameShape1 = (ImageView) findViewById(R.id.idCollageFrameShape1);
        mCollageFrameShape1.setOnClickListener(this);
        mCollageFrameShape2 = (ImageView) findViewById(R.id.idCollageFrameShape2);
        mCollageFrameShape2.setOnClickListener(this);


        mCollageEditToolsCloseLLayout = (LinearLayout) findViewById(R.id.idCollageEditToolsCloseLLayout);
        mCollageEditToolsCloseLLayout.setVisibility(View.INVISIBLE);
        mCollageEditToolsCloseLLayout.setOnClickListener(this);

        mCollageEditBackLLayout = (LinearLayout) findViewById(R.id.idCollageEditBackLLayout);
        mCollageEditBackLLayout.setOnClickListener(this);
        mCollageEditNextLLayout = (LinearLayout) findViewById(R.id.idCollageEditNextLLayout);
        mCollageEditNextLLayout.setOnClickListener(this);


        mCollageListRecycleView = (RecyclerView) findViewById(R.id.idCollageListRecycleView);
        mCollageListRecycleView.setVisibility(View.GONE);
        LinearLayoutManager mCollageFrameLManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCollageListRecycleView.setLayoutManager(mCollageFrameLManager);
        mCollageListRecycleView.setAdapter(mCollageRecycleAdapter);


        mCollageEditToolsRecycleView = (RecyclerView) findViewById(R.id.idCollageEditToolsRecycleView);
        mCollageEditToolsRecycleView.setVisibility(View.VISIBLE);
        LinearLayoutManager mCollageEditToolsLManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCollageEditToolsRecycleView.setLayoutManager(mCollageEditToolsLManager);
        mCollageEditToolsRecycleView.setAdapter(mCollageEditToolsAdapter);

    }

    private void initView3() {
        mCollageFrameShape3 = (ImageView) findViewById(R.id.idCollageFrameShape3);
        mCollageFrameShape3.setOnClickListener(this);
    }
    private void initView4() {
        mCollageFrameShape4 = (ImageView) findViewById(R.id.idCollageFrameShape4);
        mCollageFrameShape4.setOnClickListener(this);
    }
    private void initView5() {
        mCollageFrameShape5 = (ImageView) findViewById(R.id.idCollageFrameShape5);
        mCollageFrameShape5.setOnClickListener(this);
    }
    private void initView6() {
        mCollageFrameShape6 = (ImageView) findViewById(R.id.idCollageFrameShape6);
        mCollageFrameShape6.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.idCollageFrameShape1:

                if (isKitKat) {
                    Intent intentCollageShape1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intentCollageShape1.addCategory(Intent.CATEGORY_OPENABLE);
                    intentCollageShape1.setType("image/*");
                    startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_ONE);

                } else {
                    Intent intentCollageShape1 = new Intent();
                    intentCollageShape1.setType("image/*");
                    intentCollageShape1.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_ONE);
                }

                break;


            case R.id.idCollageFrameShape2:

                if (isKitKat) {
                    Intent intentCollageShape2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intentCollageShape2.addCategory(Intent.CATEGORY_OPENABLE);
                    intentCollageShape2.setType("image/*");
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_TWO);
                } else {
                    Intent intentCollageShape2 = new Intent();
                    intentCollageShape2.setType("image/*");
                    intentCollageShape2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_TWO);
                }
                break;

            case R.id.idCollageFrameShape3:

                if (isKitKat) {
                    Intent intentCollageShape2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intentCollageShape2.addCategory(Intent.CATEGORY_OPENABLE);
                    intentCollageShape2.setType("image/*");
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_THREE);
                } else {
                    Intent intentCollageShape2 = new Intent();
                    intentCollageShape2.setType("image/*");
                    intentCollageShape2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_THREE);
                }
                break;
            case R.id.idCollageFrameShape4:

                if (isKitKat) {
                    Intent intentCollageShape2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intentCollageShape2.addCategory(Intent.CATEGORY_OPENABLE);
                    intentCollageShape2.setType("image/*");
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_FOUR);
                } else {
                    Intent intentCollageShape2 = new Intent();
                    intentCollageShape2.setType("image/*");
                    intentCollageShape2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_FOUR);
                }
                break;
            case R.id.idCollageFrameShape5:

                if (isKitKat) {
                    Intent intentCollageShape2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intentCollageShape2.addCategory(Intent.CATEGORY_OPENABLE);
                    intentCollageShape2.setType("image/*");
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_FIVE);
                } else {
                    Intent intentCollageShape2 = new Intent();
                    intentCollageShape2.setType("image/*");
                    intentCollageShape2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_FIVE);
                }
                break;

            case R.id.idCollageFrameShape6:

                if (isKitKat) {
                    Intent intentCollageShape2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intentCollageShape2.addCategory(Intent.CATEGORY_OPENABLE);
                    intentCollageShape2.setType("image/*");
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_SIX);
                } else {
                    Intent intentCollageShape2 = new Intent();
                    intentCollageShape2.setType("image/*");
                    intentCollageShape2.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentCollageShape2, "Select picture"), PICK_GALLARY_REQUEST_SIX);
                }
                break;


            case R.id.idCollageEditToolsCloseLLayout:

                mCollageEditToolsCloseLLayout.setVisibility(View.INVISIBLE);
                mCollageListRecycleView.setVisibility(View.GONE);
                mCollageEditToolsRecycleView.setVisibility(View.VISIBLE);

                break;

            case R.id.idCollageEditBackLLayout:
                show_alert_back("Exit", "Are you sure you want to exit Editor ?");
                break;

            case R.id.idCollageEditNextLLayout:
                captureImage();
                mMyPrecfence.saveString(Constants.INTENT_TYPE, "CollageSelectedImage");

                try {

                    Uri uriCollageSelectedImage = Uri.fromFile(mImageFile);
                    Intent intentCollageSelectedImage = new Intent(CollageSelectedActivity.this, EditImageActivity.class);
                    intentCollageSelectedImage.putExtra(Constants.URI_COLLAGE_SELECTED_IMAGE, uriCollageSelectedImage);
                    startActivity(intentCollageSelectedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_GALLARY_REQUEST_ONE:

                if (resultCode == RESULT_OK && null != data) {
                    mBitmap1 = null;

                    Uri selectedImageUri = data.getData();
                    try {
                        mBitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //mBitmap1 = BitmapFactory.decodeFile(picturePath);

                    Matrix mat = new Matrix();
                    if (mBitmap1 != null) {
                        mBitmapRotate1 = Bitmap.createBitmap(mBitmap1, 0, 0,
                                mBitmap1.getWidth(), mBitmap1.getHeight(), mat, true);
                        mCollageFrameShape1.setImageBitmap(mBitmapRotate1);
                        mCollageFrameShape1.setOnTouchListener(this);
                    }
                }
                break;


            case PICK_GALLARY_REQUEST_TWO:

                if (resultCode == RESULT_OK && null != data) {
                    mBitmap2 = null;
                    Uri selectedImageUri = data.getData();

                    try {
                        mBitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Matrix mat = new Matrix();

                    mBitmapRotate2 = Bitmap.createBitmap(mBitmap2, 0, 0,
                            mBitmap2.getWidth(), mBitmap2.getHeight(), mat, true);
                    mCollageFrameShape2.setImageBitmap(mBitmapRotate2);
                    mCollageFrameShape2.setOnTouchListener(this);

                }
                break;
//
            case PICK_GALLARY_REQUEST_THREE:

                if (resultCode == RESULT_OK && null != data) {
                    mBitmap3 = null;
                    Uri selectedImageUri = data.getData();

                    try {
                        mBitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Matrix mat = new Matrix();

                    mBitmapRotate3 = Bitmap.createBitmap(mBitmap3, 0, 0,
                            mBitmap3.getWidth(), mBitmap3.getHeight(), mat, true);
                    mCollageFrameShape3.setImageBitmap(mBitmapRotate3);
                    mCollageFrameShape3.setOnTouchListener(this);

                }
                break;



            case PICK_GALLARY_REQUEST_FOUR:

                if (resultCode == RESULT_OK && null != data) {
                    mBitmap4 = null;
                    Uri selectedImageUri = data.getData();

                    try {
                        mBitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Matrix mat = new Matrix();

                    mBitmapRotate4 = Bitmap.createBitmap(mBitmap4, 0, 0,
                            mBitmap4.getWidth(), mBitmap4.getHeight(), mat, true);
                    mCollageFrameShape4.setImageBitmap(mBitmapRotate4);
                    mCollageFrameShape4.setOnTouchListener(this);

                }
                break;

            case PICK_GALLARY_REQUEST_FIVE:

                if (resultCode == RESULT_OK && null != data) {
                    mBitmap5 = null;
                    Uri selectedImageUri = data.getData();

                    try {
                        mBitmap5 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Matrix mat = new Matrix();

                    mBitmapRotate5 = Bitmap.createBitmap(mBitmap5, 0, 0,
                            mBitmap5.getWidth(), mBitmap5.getHeight(), mat, true);
                    mCollageFrameShape5.setImageBitmap(mBitmapRotate5);
                    mCollageFrameShape5.setOnTouchListener(this);

                }
                break;

            case PICK_GALLARY_REQUEST_SIX:

                if (resultCode == RESULT_OK && null != data) {
                    mBitmap6 = null;
                    Uri selectedImageUri = data.getData();

                    try {
                        mBitmap6 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Matrix mat = new Matrix();
                    mBitmapRotate6 = Bitmap.createBitmap(mBitmap6, 0, 0,
                            mBitmap6.getWidth(), mBitmap6.getHeight(), mat, true);
                    mCollageFrameShape6.setImageBitmap(mBitmapRotate6);
                    mCollageFrameShape6.setOnTouchListener(this);
                }
                break;





            case PICK_CAMERA_REQUEST_ONE:

                if (resultCode == RESULT_OK && null != data) {
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                        Bitmap bm1 = BitmapFactory.decodeFile(f.getAbsolutePath(),
                                btmapOptions);

                        Matrix mat = new Matrix();
                        Bitmap bMapRotate = Bitmap.createBitmap(bm1, 0, 0,
                                bm1.getWidth(), bm1.getHeight(), mat, true);
                        mCollageFrameShape1.setImageBitmap(bMapRotate);
                        mCollageFrameShape1.setOnTouchListener(this);
                        String path = Environment.getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();
                        OutputStream fOut = null;
                        File file = new File(path, String.valueOf(System
                                .currentTimeMillis()) + ".jpg");
                        try {
                            fOut = new FileOutputStream(file);
                            bm1.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                break;


            case PICK_CAMERA_REQUEST_TWO:

                if (resultCode == RESULT_OK && null != data) {
                    File f = new File(Environment.getExternalStorageDirectory()
                            .toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                        Bitmap bm1 = BitmapFactory.decodeFile(f.getAbsolutePath(),
                                btmapOptions);

                        Matrix mat = new Matrix();
                        Bitmap bMapRotate = Bitmap.createBitmap(bm1, 0, 0,
                                bm1.getWidth(), bm1.getHeight(), mat, true);
                        mCollageFrameShape2.setImageBitmap(bMapRotate);
                        mCollageFrameShape2.setOnTouchListener(this);

                        String path = Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";
                        f.delete();
                        OutputStream fOut = null;
                        File file = new File(path, String.valueOf(System
                                .currentTimeMillis()) + ".jpg");
                        try {
                            fOut = new FileOutputStream(file);
                            bm1.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;

        if (view == mCollageFrameShape1) {

            isSelectedImage1 = true;
            isSelectedImage2 = false;
            isSelectedImage3 = false;
            isSelectedImage4 = false;
            isSelectedImage5 = false;
            isSelectedImage6 = false;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mMatrixSaved1.set(mMatrixShape1);

                    startPointF.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;


                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        mMatrixSaved1.set(mMatrixShape1);
                        midPoint(midPointF, event);
                        mode = ZOOM;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        mMatrixShape1.set(mMatrixSaved1);
                        mMatrixShape1.postTranslate(event.getX() - startPointF.x, event.getY()
                                - startPointF.y);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            mMatrixShape1.set(mMatrixSaved1);
                            float scale = newDist / oldDist;
                            mMatrixShape1.postScale(scale, scale, midPointF.x, midPointF.y);
                        }
                    }
                    break;
            }
            view.setImageMatrix(mMatrixShape1);


        } else if (view == mCollageFrameShape2) {

            isSelectedImage1 = false;
            isSelectedImage2 = true;
            isSelectedImage3 = false;
            isSelectedImage4 = false;
            isSelectedImage5 = false;
            isSelectedImage6 = false;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mMatrixSaved2.set(mMatrixShape2);

                    startPointF.set(event.getX(), event.getY());
                    Log.d(TAG, "mode=DRAG");
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    Log.d(TAG, "oldDist=" + oldDist);
                    if (oldDist > 10f) {
                        mMatrixSaved2.set(mMatrixShape2);
                        midPoint(midPointF, event);
                        mode = ZOOM;
                        Log.d(TAG, "mode=ZOOM");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    Log.d(TAG, "mode=NONE");
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        mMatrixShape2.set(mMatrixSaved2);
                        mMatrixShape2.postTranslate(event.getX() - startPointF.x, event.getY()
                                - startPointF.y);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        Log.d(TAG, "newDist=" + newDist);
                        if (newDist > 10f) {
                            mMatrixShape2.set(mMatrixSaved2);
                            float scale = newDist / oldDist;
                            mMatrixShape2.postScale(scale, scale, midPointF.x, midPointF.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(mMatrixShape2);

        }


        else if (view == mCollageFrameShape3) {
            isSelectedImage1 = false;
            isSelectedImage2 = false;
            isSelectedImage3 = true;
            isSelectedImage4 = false;
            isSelectedImage5 = false;
            isSelectedImage6 = false;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mMatrixSaved3.set(mMatrixShape3);
                    startPointF.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        mMatrixSaved3.set(mMatrixShape3);
                        midPoint(midPointF, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        mMatrixShape3.set(mMatrixSaved3);
                        mMatrixShape3.postTranslate(event.getX() - startPointF.x, event.getY()
                                - startPointF.y);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            mMatrixShape3.set(mMatrixSaved3);
                            float scale = newDist / oldDist;
                            mMatrixShape3.postScale(scale, scale, midPointF.x, midPointF.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(mMatrixShape3);

        }

        else if (view == mCollageFrameShape4) {

            isSelectedImage1 = false;
            isSelectedImage2 = false;
            isSelectedImage3 = false;
            isSelectedImage4 = true;
            isSelectedImage5 = false;
            isSelectedImage6 = false;

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mMatrixSaved4.set(mMatrixShape4);
                    startPointF.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        mMatrixSaved4.set(mMatrixShape4);
                        midPoint(midPointF, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        mMatrixShape4.set(mMatrixSaved4);
                        mMatrixShape4.postTranslate(event.getX() - startPointF.x, event.getY()
                                - startPointF.y);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            mMatrixShape4.set(mMatrixSaved4);
                            float scale = newDist / oldDist;
                            mMatrixShape4.postScale(scale, scale, midPointF.x, midPointF.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(mMatrixShape4);

        }

        else if (view == mCollageFrameShape5) {
            isSelectedImage1 = false;
            isSelectedImage2 = false;
            isSelectedImage3 = false;
            isSelectedImage4 = false;
            isSelectedImage5 = true;
            isSelectedImage6 = false;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mMatrixSaved5.set(mMatrixShape5);
                    startPointF.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        mMatrixSaved5.set(mMatrixShape5);
                        midPoint(midPointF, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        mMatrixShape5.set(mMatrixSaved5);
                        mMatrixShape5.postTranslate(event.getX() - startPointF.x, event.getY()
                                - startPointF.y);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            mMatrixShape5.set(mMatrixSaved5);
                            float scale = newDist / oldDist;
                            mMatrixShape5.postScale(scale, scale, midPointF.x, midPointF.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(mMatrixShape5);

        }

        else if (view == mCollageFrameShape6) {
            isSelectedImage1 = false;
            isSelectedImage2 = false;
            isSelectedImage3 = false;
            isSelectedImage4 = false;
            isSelectedImage5 = false;
            isSelectedImage6 = true;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mMatrixSaved6.set(mMatrixShape6);
                    startPointF.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        mMatrixSaved6.set(mMatrixShape6);
                        midPoint(midPointF, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        mMatrixShape6.set(mMatrixSaved6);
                        mMatrixShape6.postTranslate(event.getX() - startPointF.x, event.getY()
                                - startPointF.y);

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            mMatrixShape6.set(mMatrixSaved6);
                            float scale = newDist / oldDist;
                            mMatrixShape6.postScale(scale, scale, midPointF.x, midPointF.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(mMatrixShape6);

        }
        return true;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);

    }

    /**
     * Calculate the midPointF point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    File mImageFile;

    private String captureImage() {

        OutputStream output;
        Calendar cal = Calendar.getInstance();

        Bitmap bitmap = Bitmap.createBitmap(mCollageRLayout.getWidth(), mCollageRLayout.getHeight(), Bitmap.Config.ARGB_8888);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, mCollageRLayout.getWidth(), mCollageRLayout.getHeight());

        Canvas mBitCanvas = new Canvas(bitmap);
        mCollageRLayout.draw(mBitCanvas);

        // Find the SD Card path
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/SelfiePro/");
        dir.mkdirs();

        mImageName = "SelfiePro" + cal.getTimeInMillis() + ".png";
        // Create a name for the saved image
        mImageFile = new File(dir, mImageName);

        // Show a toast message on successful save
        Toast.makeText(CollageSelectedActivity.this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();

        try {
            output = new FileOutputStream(mImageFile);
            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mImageName;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        show_alert_back("Exit", "Are you sure you want to exit Editor ?");
        return super.onKeyDown(keyCode, event);

    }


    public void show_alert_back(String title, String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CollageSelectedActivity.this);
        // set title
        alertDialogBuilder.setTitle(title);
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(true)
                .setIcon(R.drawable.ic_launcher).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, close
                mMyPrecfence.saveString(Constants.COLLAGE_ACTIVITY , "false");
                // current activity
                finish();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        // show it
        alertDialog.show();

        Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button b1 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        if (b != null)
            b.setTextColor(getResources().getColor(R.color.colorAccent));
        if (b1 != null)
            b1.setTextColor(getResources().getColor(R.color.colorAccent));
    }


    /*****************************New**********************************/

    boolean isSelectedFrame2_1 = false;
    boolean isSelectedFrame2_2 = false;
    boolean isSelectedFrame3_1 = false;
    boolean isSelectedFrame3_2 = false;
    boolean isSelectedFrame3_3 = false;
    boolean isSelectedFrame4_1 = false;
    boolean isSelectedFrame4_2 = false;
    boolean isSelectedFrame4_3 = false;
    boolean isSelectedFrame4_4 = false;
    boolean isSelectedFrame6_1 = false;

    @Override
    public void onCollageFrameSelected(int collageFrameID, CollageFrameType collageFrameType) {

        //mMainCollageFrameImage = (ImageView) findViewById(R.id.idMainCollageFrameImage);
        mMainCollageFrameImage.setBackgroundResource(collageFrameID);
        mCollageEditToolsCloseLLayout.setVisibility(View.INVISIBLE);
        mCollageListRecycleView.setVisibility(View.GONE);
        mCollageEditToolsRecycleView.setVisibility(View.VISIBLE);



        switch (collageFrameType) {

            case TWO_ONE:

                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;
                if(isSelectedFrame2_1){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }

                setContentView(R.layout.activity_collage_2_1);
                initView();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);

                Matrix mat = new Matrix();
                if (mBitmap1 != null && mBitmapRotate1!=null) {
                    mBitmapRotate1 = Bitmap.createBitmap(mBitmap1, 0, 0,
                            mBitmap1.getWidth(), mBitmap1.getHeight(), mat, true);
                    mCollageFrameShape1.setImageBitmap(mBitmapRotate1);
                    mCollageFrameShape1.setOnTouchListener(this);
                }
                if (mBitmap2 != null && mBitmapRotate2!=null) {
                    mBitmapRotate2 = Bitmap.createBitmap(mBitmap2, 0, 0,
                            mBitmap2.getWidth(), mBitmap2.getHeight(), mat, true);
                    mCollageFrameShape2.setImageBitmap(mBitmapRotate2);
                    mCollageFrameShape2.setOnTouchListener(this);
                }
//                if(mBitmapRotate1!=null){
//                    mCollageFrameShape1.setImageBitmap(mBitmapRotate1);
//                }

//                if(mBitmapRotate2!=null){
//                    mCollageFrameShape2.setImageBitmap(mBitmapRotate2);
//                }


                isSelectedFrame2_1 = true;

                break;

            case TWO_TWO:

                isSelectedFrame2_1 = false;

                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;

                if(isSelectedFrame2_2){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_2_2);
                initView();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);

                Matrix mat22 = new Matrix();
                if (mBitmap1 != null && mBitmapRotate1!=null) {
                    mBitmapRotate1 = Bitmap.createBitmap(mBitmap1, 0, 0,
                            mBitmap1.getWidth(), mBitmap1.getHeight(), mat22, true);
                    mCollageFrameShape1.setImageBitmap(mBitmapRotate1);
                    mCollageFrameShape1.setOnTouchListener(this);
                }
                if (mBitmap2 != null && mBitmapRotate2!=null) {
                    mBitmapRotate2 = Bitmap.createBitmap(mBitmap2, 0, 0,
                            mBitmap2.getWidth(), mBitmap2.getHeight(), mat22, true);
                    mCollageFrameShape2.setImageBitmap(mBitmapRotate2);
                    mCollageFrameShape2.setOnTouchListener(this);
                }

//                if(mBitmapRotate1!=null){
//                    mCollageFrameShape1.setImageBitmap(mBitmapRotate1);
//                }
//                if(mBitmapRotate2!=null){
//                    mCollageFrameShape2.setImageBitmap(mBitmapRotate2);
//                }


                isSelectedFrame2_2 = true;

                break;

            case THREE_ONE:

                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                //isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;
                if(isSelectedFrame3_1){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_3_1);
                initView();
                initView3();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame3_1 = true;
                break;


            case THREE_TWO:
                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                //isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;
                if(isSelectedFrame3_2){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_3_2);
                initView();
                initView3();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame3_2 = true;
                break;


            case THREE_THREE:
                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                //isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;

                if(isSelectedFrame3_3){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_3_3);
                initView();
                initView3();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame3_3 = true;
                break;

            case FOUR_ONE:
                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                //isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;

                if(isSelectedFrame4_1){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_4_1);
                initView();
                initView3();
                initView4();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame4_1 = true;
                break;

            case FOUR_TWO:
                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                //isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;
                if(isSelectedFrame4_2){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_4_2);
                initView();
                initView3();
                initView4();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame4_2 = true;
                break;

            case FOUR_THREE:
                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                //isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;
                if(isSelectedFrame4_3){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_4_3);
                initView();
                initView3();
                initView4();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame4_3 = true;

                break;

            case FOUR_FOUR:
                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                //isSelectedFrame4_4 = false;
                isSelectedFrame6_1 = false;
                if(isSelectedFrame4_4){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_4_4);
                initView();
                initView3();
                initView4();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame4_4 = true;

                break;

            case SIX_ONE:
                isSelectedFrame2_1 = false;
                isSelectedFrame2_2 = false;
                isSelectedFrame3_1 = false;
                isSelectedFrame3_2 = false;
                isSelectedFrame3_3 = false;
                isSelectedFrame4_1 = false;
                isSelectedFrame4_2 = false;
                isSelectedFrame4_3 = false;
                isSelectedFrame4_4 = false;
                //isSelectedFrame6_1 = false;
                if(isSelectedFrame6_1){
                    Toast.makeText(this , "You have already selected." , Toast.LENGTH_SHORT ).show();
                    return;
                }
                setContentView(R.layout.activity_collage_6_1);
                initView();
                initView3();
                initView4();
                initView5();
                initView6();
                mMainCollageFrameImage.setBackgroundResource(collageFrameID);
                isSelectedFrame6_1 = true;
                break;
            default:
                //setContentView(R.layout.activity_collage_selected_image);
                break;
        }

    }


    @Override
    public void onCollageEditTollsSelected(CollageEditToolsType collageEditToolsType) {
        switch (collageEditToolsType) {


            case COLLAGE:
                mCollageEditToolsCloseLLayout.setVisibility(View.VISIBLE);
                mCollageEditToolsRecycleView.setVisibility(View.GONE);
                mCollageListRecycleView.setVisibility(View.VISIBLE);
                break;


            case CLOSE:
                show_alert_back("Exit", "Are you sure you want to exit Editor ?");
                break;

            case SAVE:
                String sCollageFileName = captureImage();
                break;

            case RT_LEFT:
                if (isSelectedImage1) {
                    mMatrixShape1.postRotate(-12, mCollageFrameShape1.getMeasuredWidth() / 2,
                            mCollageFrameShape1.getMeasuredHeight() / 2);
                    mCollageFrameShape1.setImageMatrix(mMatrixShape1);
                } else if (isSelectedImage2) {
                    mMatrixShape2.postRotate(-12, mCollageFrameShape2.getMeasuredWidth() / 2,
                            mCollageFrameShape2.getMeasuredHeight() / 2);
                    mCollageFrameShape2.setImageMatrix(mMatrixShape2);
                } else if (isSelectedImage3) {
                    mMatrixShape3.postRotate(-12, mCollageFrameShape3.getMeasuredWidth() / 2,
                            mCollageFrameShape3.getMeasuredHeight() / 2);
                    mCollageFrameShape3.setImageMatrix(mMatrixShape3);
                }
                else if (isSelectedImage4) {
                    mMatrixShape4.postRotate(-12, mCollageFrameShape4.getMeasuredWidth() / 2,
                            mCollageFrameShape4.getMeasuredHeight() / 2);
                    mCollageFrameShape4.setImageMatrix(mMatrixShape4);
                }
                else if (isSelectedImage5) {
                    mMatrixShape5.postRotate(-12, mCollageFrameShape5.getMeasuredWidth() / 2,
                            mCollageFrameShape5.getMeasuredHeight() / 2);
                    mCollageFrameShape5.setImageMatrix(mMatrixShape5);
                }
                else if (isSelectedImage6) {
                    mMatrixShape6.postRotate(-12, mCollageFrameShape6.getMeasuredWidth() / 2,
                            mCollageFrameShape6.getMeasuredHeight() / 2);
                    mCollageFrameShape6.setImageMatrix(mMatrixShape6);
                }
                break;

            case RT_RIGHT:
                if (isSelectedImage1) {
                    mMatrixShape1.postRotate(12, mCollageFrameShape1.getMeasuredWidth() / 2,
                            mCollageFrameShape1.getMeasuredHeight() / 2);
                    mCollageFrameShape1.setImageMatrix(mMatrixShape1);
                } else if (isSelectedImage2) {
                    mMatrixShape2.postRotate(12, mCollageFrameShape2.getMeasuredWidth() / 2,
                            mCollageFrameShape2.getMeasuredHeight() / 2);
                    mCollageFrameShape2.setImageMatrix(mMatrixShape2);
                } else if (isSelectedImage3) {
                    mMatrixShape3.postRotate(12, mCollageFrameShape3.getMeasuredWidth() / 2,
                            mCollageFrameShape3.getMeasuredHeight() / 2);
                    mCollageFrameShape3.setImageMatrix(mMatrixShape3);
                }
                else if (isSelectedImage4) {
                    mMatrixShape4.postRotate(12, mCollageFrameShape4.getMeasuredWidth() / 2,
                            mCollageFrameShape4.getMeasuredHeight() / 2);
                    mCollageFrameShape4.setImageMatrix(mMatrixShape4);
                }
                else if (isSelectedImage5) {
                    mMatrixShape5.postRotate(12, mCollageFrameShape5.getMeasuredWidth() / 2,
                            mCollageFrameShape5.getMeasuredHeight() / 2);
                    mCollageFrameShape5.setImageMatrix(mMatrixShape5);
                }
                else if (isSelectedImage6) {
                    mMatrixShape6.postRotate(12, mCollageFrameShape6.getMeasuredWidth() / 2,
                            mCollageFrameShape6.getMeasuredHeight() / 2);
                    mCollageFrameShape6.setImageMatrix(mMatrixShape6);
                }
                break;


            case GALLERY:
                if (isSelectedImage1) {

                    if (isKitKat) {
                        Intent intentCollageShape1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intentCollageShape1.addCategory(Intent.CATEGORY_OPENABLE);
                        intentCollageShape1.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_ONE);
                    } else {
                        Intent intentCollageShape1 = new Intent();
                        intentCollageShape1.setType("image/*");
                        intentCollageShape1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_ONE);
                    }


                } else if (isSelectedImage2) {

                    if (isKitKat) {
                        Intent intentCollageShape1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intentCollageShape1.addCategory(Intent.CATEGORY_OPENABLE);
                        intentCollageShape1.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_TWO);
                    } else {
                        Intent intentCollageShape1 = new Intent();
                        intentCollageShape1.setType("image/*");
                        intentCollageShape1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_TWO);
                    }

                } else if (isSelectedImage3) {

                    if (isKitKat) {
                        Intent intentCollageShape1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intentCollageShape1.addCategory(Intent.CATEGORY_OPENABLE);
                        intentCollageShape1.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_THREE);
                    } else {
                        Intent intentCollageShape1 = new Intent();
                        intentCollageShape1.setType("image/*");
                        intentCollageShape1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_THREE);
                    }

                }
                else if (isSelectedImage4) {

                    if (isKitKat) {
                        Intent intentCollageShape1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intentCollageShape1.addCategory(Intent.CATEGORY_OPENABLE);
                        intentCollageShape1.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_FOUR);
                    } else {
                        Intent intentCollageShape1 = new Intent();
                        intentCollageShape1.setType("image/*");
                        intentCollageShape1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_FOUR);
                    }

                }
                else if (isSelectedImage5) {

                    if (isKitKat) {
                        Intent intentCollageShape1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intentCollageShape1.addCategory(Intent.CATEGORY_OPENABLE);
                        intentCollageShape1.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_FIVE);
                    } else {
                        Intent intentCollageShape1 = new Intent();
                        intentCollageShape1.setType("image/*");
                        intentCollageShape1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_FIVE);
                    }

                }
                else if (isSelectedImage6) {
                    if (isKitKat) {
                        Intent intentCollageShape1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intentCollageShape1.addCategory(Intent.CATEGORY_OPENABLE);
                        intentCollageShape1.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_SIX);
                    } else {
                        Intent intentCollageShape1 = new Intent();
                        intentCollageShape1.setType("image/*");
                        intentCollageShape1.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intentCollageShape1, "Select picture"), PICK_GALLARY_REQUEST_SIX);
                    }
                }
                break;

            case SHARE:
                String filename = captureImage();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.fromFile(mImageFile);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                break;
        }
    }
}
