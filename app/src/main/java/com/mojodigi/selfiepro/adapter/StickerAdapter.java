package com.mojodigi.selfiepro.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.interfaces.StickerListener;


public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    private Context mContext;
    private StickerListener mStickerListener;
    private int[] mStickerList ;

    public StickerAdapter(Context context ) {
        this.mContext = context;

    }

    public StickerAdapter(Context context, StickerListener stickerListener, int[] stickerList) {
        this.mContext = context;
        this.mStickerListener = stickerListener;
        this.mStickerList = stickerList;
    }

    @Override
    public StickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker, parent, false);
        return new StickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StickerAdapter.ViewHolder viewHolder,   final int position) {
       viewHolder.imgSticker.setImageResource(mStickerList[position]);

//        viewHolder.imgSticker.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (mStickerListener != null) {
//                    mStickerListener.onStickerClick(BitmapFactory.decodeResource(mContext.getResources(),
//                            mStickerList[position]));
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mStickerList.length;
    }






     class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        public ImageView imgSticker;


        ViewHolder(View itemView) {
            super(itemView);

            this.mItemView = itemView;

            imgSticker = itemView.findViewById(R.id.imgSticker);

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStickerListener != null) {
                        mStickerListener.onStickerClick(
                                BitmapFactory.decodeResource(mContext.getResources(),   mStickerList[getLayoutPosition()]));
                    }

                }
            });


        }
    }
}