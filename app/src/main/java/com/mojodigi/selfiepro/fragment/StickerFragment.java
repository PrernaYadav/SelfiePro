package com.mojodigi.selfiepro.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.adapter.StickerAdapter;
import com.mojodigi.selfiepro.interfaces.StickerListener;



public class StickerFragment extends BottomSheetDialogFragment implements StickerListener  {

    private StickerListener mStickerListener;
    private StickerAdapter mStickerAdapter ;
    private int[] mStickerList  ;
    private RecyclerView mRecycleViewEmoji ;

    public StickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setStickerListener(StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        mRecycleViewEmoji = contentView.findViewById(R.id.rvEmoji);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        mRecycleViewEmoji.setLayoutManager(gridLayoutManager);


        mStickerList = new int[]{R.drawable.sticker_tager1, R.drawable.sticker_tager2, R.drawable.sticker_tager3, R.drawable.sticker_tager4, R.drawable.sticker_tager5
                , R.drawable.sticker_01,R.drawable.sticker_02,R.drawable.sticker_03,R.drawable.sticker_04 ,
                R.drawable.sticker_05,  R.drawable.sticker_06,  R.drawable.sticker_07, R.drawable.sticker_08,
                R.drawable.sticker_09 , R.drawable.sticker_10};


        mStickerAdapter = new StickerAdapter(getActivity(), mStickerListener , mStickerList);
        mRecycleViewEmoji.setAdapter(mStickerAdapter);

    }

    /*StickerListener Interface Method*/
    @Override
    public void onStickerClick(Bitmap bitmap) {
        dismiss();
    }




    private String convertEmoji(String emoji) {
        String returnedEmoji = "";
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
        } catch (NumberFormatException e) {
            returnedEmoji = "";
        }
        return returnedEmoji;
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }












}