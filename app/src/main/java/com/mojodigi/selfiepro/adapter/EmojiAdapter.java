package com.mojodigi.selfiepro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.interfaces.EmojiListener;

import java.util.ArrayList;



public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mEmojisList  ;
    private EmojiListener mEmojiListener  ;


    public EmojiAdapter(Context context, EmojiListener emojiListener, ArrayList<String> emojisList) {
        this.mContext = context;
        this.mEmojiListener = emojiListener;
        this.mEmojisList = emojisList;
    }

    @Override
    public EmojiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emoji, parent, false);
        return new EmojiAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(EmojiAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.txtEmoji.setText(mEmojisList.get(position));

//        viewHolder.txtEmoji.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mEmojiListener != null) {
//                    mEmojiListener.onEmojiClick(mEmojisList.get(position));
//                }
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mEmojisList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        private TextView txtEmoji;

        ViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;

            txtEmoji = itemView.findViewById(R.id.txtEmoji);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEmojiListener != null) {
                        mEmojiListener.onEmojiClick(mEmojisList.get(getLayoutPosition()));
                    }

                }
            });
        }
    }
}