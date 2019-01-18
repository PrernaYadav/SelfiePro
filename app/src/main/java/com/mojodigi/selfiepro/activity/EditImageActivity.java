package com.mojodigi.selfiepro.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.adapter.EditingToolsAdapter;
import com.mojodigi.selfiepro.adapter.FilterViewAdapter;
import com.mojodigi.selfiepro.base.BaseActivity;
import com.mojodigi.selfiepro.collage.CollageSelectedActivity;
import com.mojodigi.selfiepro.enums.ToolType;
import com.mojodigi.selfiepro.interfaces.FilterListener;
import com.mojodigi.selfiepro.fragment.EmojiFragment;
import com.mojodigi.selfiepro.fragment.PropertiesFragment;
import com.mojodigi.selfiepro.fragment.StickerFragment;
import com.mojodigi.selfiepro.fragment.TextEditorFragment;
import com.mojodigi.selfiepro.interfaces.EmojiListener;
import com.mojodigi.selfiepro.interfaces.OnItemSelected;
import com.mojodigi.selfiepro.interfaces.Properties;
import com.mojodigi.selfiepro.interfaces.StickerListener;
import com.mojodigi.selfiepro.utils.Constants;
import com.mojodigi.selfiepro.utils.MyPreference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;


public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener, Properties, EmojiListener,
        StickerListener, OnItemSelected, FilterListener   /* , View.OnTouchListener*/ {

    private static final String TAG = EditImageActivity.class.getSimpleName();


    private static final int PICK_CAMERA_REQUEST = 1;
    private static final int PICK_GALLARY_REQUEST = 2;

    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;

    private PropertiesFragment mPropertiesFragment;
    private EmojiFragment mEmojiFragment;
    private StickerFragment mStickerFragment;

    private TextView mTxtCurrentTool;
    private Typeface mWonderFont;

    private RecyclerView mRvTools, mRvFilters;

    private EditingToolsAdapter mEditingToolsAdapter  ;
    private FilterViewAdapter mFilterViewAdapter  ;

    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet  ;
    private boolean mIsFilterVisible;

    private ImageView imgUndo, imgRedo, imgCollage, imgCamera, imgGallery, imgSave, imgClose;

    private boolean isKitKat ;

    private MyPreference mMyPrecfence = null;

    private String mIntentType = "";
    private Typeface mEmojiTypeFace ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeFullScreen();

        setContentView(R.layout.activity_edit_image);

        initViews();
    }


    private void initViews() {

        isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        mConstraintSet = new ConstraintSet();
        mEditingToolsAdapter = new EditingToolsAdapter(this);
        mFilterViewAdapter = new FilterViewAdapter(this);

        if (mMyPrecfence == null) {
            mMyPrecfence = MyPreference.getMyPreferenceInstance(EditImageActivity.this);
        }

        try{
            mMyPrecfence.saveString(Constants.COLLAGE_ACTIVITY , "false");
            mIntentType = mMyPrecfence.getString(Constants.INTENT_TYPE);
        }catch (Exception ex){
            ex.getStackTrace();
        }



        mPhotoEditorView = findViewById(R.id.photoEditorView);


        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        mRvTools = findViewById(R.id.rvConstraintTools);


        mRvFilters = findViewById(R.id.rvFilterView);


        mRootView = findViewById(R.id.rootView);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgCollage = findViewById(R.id.imgCollage);
        imgCollage.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        mPropertiesFragment = new PropertiesFragment();
        mEmojiFragment = new EmojiFragment();
        mStickerFragment = new StickerFragment();

        /*Interface Listener*/
        mStickerFragment.setStickerListener(this);
        mEmojiFragment.setEmojiListener(this);
        mPropertiesFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);


        //Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);
        mEmojiTypeFace = Typeface.createFromAsset(getAssets(), "emojione-android.ttf");

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

        //Set Image Dynamically
        // mPhotoEditorView.getSource().setImageResource(R.drawable.color_palette);


        if (mIntentType.equalsIgnoreCase("IntentCamera")) {
            mPhotoEditor.clearAllViews();
            Intent extrasIntentCamera = getIntent();
            if (extrasIntentCamera != null) {
                Bitmap photoCameraBitmap = (Bitmap) this.getIntent().getParcelableExtra("BITMAP_PICK_CAMERA");
                mPhotoEditorView.getSource().setImageBitmap(photoCameraBitmap);
            }
        }



        if (mIntentType.equalsIgnoreCase("IntentGallery")) {
            mPhotoEditor.clearAllViews();
            Uri myUri = null;
            Intent extrasIntent = getIntent();
            if (extrasIntent != null) {
                myUri = extrasIntent.getParcelableExtra("URI_PICK_GALLARY");
            }
            try {
                Bitmap bitmapPhotoGallary = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
                mPhotoEditorView.getSource().setImageBitmap(bitmapPhotoGallary);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mIntentType.equalsIgnoreCase("CollageSelectedImage")) {
            mPhotoEditor.clearAllViews();
            Uri mUriCollageSelectedImage = null;
            Intent extrasIntent = getIntent();
            if (extrasIntent != null) {
                mUriCollageSelectedImage = extrasIntent.getParcelableExtra(Constants.URI_COLLAGE_SELECTED_IMAGE);
            }
            try {
                Bitmap bitmapPhotoGallary = MediaStore.Images.Media.getBitmap(getContentResolver(), mUriCollageSelectedImage);
                mPhotoEditorView.getSource().setImageBitmap(bitmapPhotoGallary);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage();
                break;

            case R.id.imgClose:
                onBackPressed();
                break;

            case R.id.imgCollage:
                mMyPrecfence.saveString(Constants.INTENT_TYPE, "IntentCollage");
                //Intent collageIntent = new Intent(this, CollageFramesActivity.class);
                Intent collageIntent = new Intent(this, CollageSelectedActivity.class);
                startActivity(collageIntent);
                break;

            case R.id.imgCamera:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_CAMERA_REQUEST);
                break;

            case R.id.imgGallery:
                openGallery();
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
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextEditorFragment textEditorDialogFragment =
                TextEditorFragment.show(this, text, colorCode);


        textEditorDialogFragment.setOnTextEditorListener(new TextEditorFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                mPhotoEditor.editText(rootView, inputText, colorCode);
                mTxtCurrentTool.setText(R.string.label_text);
            }
        });
    }



    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }


    /******************************************************************************************************/


    @SuppressLint("MissingPermission")
    private void saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            showLoading("Saving...");

            Calendar cal = Calendar.getInstance();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SelfiePro/"
                    + File.separator + "SelfiePro"+cal.getTimeInMillis()+ ".png");
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();

                        showSnackbar("Image Saved Successfully");

                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                        showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }
    }

    @Override
    public void isPermissionGranted(boolean isGranted, String permission) {
        if (isGranted) {
            saveImage();
        }
    }




    /********************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case PICK_CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mPhotoEditorView.getSource().setImageBitmap(photo);
                    break;

                case PICK_GALLARY_REQUEST:
                    try {
                        mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;


            }
        }
    }


    /***********************************************************************************************/

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
        //main_img.setBrushColor(colorCode);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        //mPhotoEditor.addEmoji(emojiUnicode);
        //main_img.addEmoji(emojiUnicode);
        mPhotoEditor.addEmoji(mEmojiTypeFace, emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
        mTxtCurrentTool.setText(R.string.label_sticker);
    }


    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit").setIcon(R.drawable.ic_launcher).setMessage("Are you want to exit without saving image ?");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }


    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }


    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {

            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mTxtCurrentTool.setText(R.string.label_brush);
                mPropertiesFragment.show(getSupportFragmentManager(), mPropertiesFragment.getTag());
                break;

            case TEXT:
                TextEditorFragment textEditorDialogFragment = TextEditorFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        mPhotoEditor.addText(inputText, colorCode);
                        mTxtCurrentTool.setText(R.string.label_text);
                    }
                });
                break;

            case ERASER:
                mPhotoEditor.brushEraser();
                mTxtCurrentTool.setText(R.string.label_eraser);
                break;

            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;

            case EMOJI:
                mEmojiFragment.show(getSupportFragmentManager(), mEmojiFragment.getTag());
                break;

            case STICKER:
                mStickerFragment.show(getSupportFragmentManager(), mStickerFragment.getTag());
                break;
        }
    }



    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }



    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (!mPhotoEditor.isCacheEmpty()) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }


}
