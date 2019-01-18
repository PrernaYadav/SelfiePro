package com.mojodigi.selfiepro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.enums.CollageEditToolsType;
import com.mojodigi.selfiepro.interfaces.OnCollageEditTollsSelected;
import com.mojodigi.selfiepro.model.CollageEditToolsModel;

import java.util.ArrayList;
import java.util.List;

public class CollageEditToolsAdapter extends RecyclerView.Adapter<CollageEditToolsAdapter.ViewHolder> {

    private List<CollageEditToolsModel> mCollageEditToolsList  ;

    private OnCollageEditTollsSelected mOnCollageEditTollsSelected;



    public CollageEditToolsAdapter(OnCollageEditTollsSelected onCollageEditTollsSelected) {

        mOnCollageEditTollsSelected = onCollageEditTollsSelected;
        mCollageEditToolsList = new ArrayList<>();

        //mCollageEditToolsList.add(new CollageEditToolsModel("Close", R.drawable.ic_close, CollageEditToolsType.CLOSE));
        mCollageEditToolsList.add(new CollageEditToolsModel("Collage", R.drawable.ic_collage_white, CollageEditToolsType.COLLAGE));
        mCollageEditToolsList.add(new CollageEditToolsModel("Gallery", R.drawable.ic_image_white, CollageEditToolsType.GALLERY));
        mCollageEditToolsList.add(new CollageEditToolsModel("Rt Left", R.drawable.ic_undo, CollageEditToolsType.RT_LEFT));
        mCollageEditToolsList.add(new CollageEditToolsModel("Rt Right", R.drawable.ic_redo, CollageEditToolsType.RT_RIGHT));
        mCollageEditToolsList.add(new CollageEditToolsModel("Save", R.drawable.ic_save, CollageEditToolsType.SAVE));
        mCollageEditToolsList.add(new CollageEditToolsModel("Share", R.drawable.ic_share, CollageEditToolsType.SHARE));

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_collage_edittools, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollageEditToolsModel item = mCollageEditToolsList.get(position);
        holder.mCollageEditToolsText.setText(item.mToolName);
        holder.mCollageEditToolsImgView.setImageResource(item.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return mCollageEditToolsList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mCollageEditToolsImgView;
        TextView mCollageEditToolsText;

        ViewHolder(View itemView) {
            super(itemView);

            mCollageEditToolsImgView = itemView.findViewById(R.id.idCollageEditToolsImgView);
            mCollageEditToolsText = itemView.findViewById(R.id.idCollageEditToolsText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCollageEditTollsSelected.onCollageEditTollsSelected(mCollageEditToolsList.get(getLayoutPosition()).mCollageEditToolsType);
                }
            });
        }
    }


}

