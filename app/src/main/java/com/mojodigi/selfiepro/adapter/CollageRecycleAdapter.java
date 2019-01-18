package com.mojodigi.selfiepro.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.enums.CollageFrameType;
import com.mojodigi.selfiepro.interfaces.OnCollageFrameSelected;
import com.mojodigi.selfiepro.model.CollageFrameModel;
import java.util.ArrayList;
import java.util.List;



public class CollageRecycleAdapter extends RecyclerView.Adapter<CollageRecycleAdapter.ViewHolder> {

    private Context mContext ;
    private List<CollageFrameModel> mCollageFrameList ;
    private OnCollageFrameSelected mOnCollageFrameSelected;


    public  CollageRecycleAdapter(Context context, ArrayList<CollageFrameModel> collageFrameList , OnCollageFrameSelected onCollageFrameSelected){

        this.mContext = context;
        this.mCollageFrameList = collageFrameList;
        this.mOnCollageFrameSelected = onCollageFrameSelected;

    }

    public CollageRecycleAdapter(OnCollageFrameSelected onCollageFrameSelected) {

        mOnCollageFrameSelected = onCollageFrameSelected;

        mCollageFrameList = new ArrayList<>();

        mCollageFrameList.add(new CollageFrameModel("One", R.drawable.collage2_1, R.drawable.collage2_1, CollageFrameType.TWO_ONE));
        mCollageFrameList.add(new CollageFrameModel("Two", R.drawable.collage2_2, R.drawable.collage2_2,CollageFrameType.TWO_TWO));
        mCollageFrameList.add(new CollageFrameModel("Three", R.drawable.collage3_1, R.drawable.collage3_1, CollageFrameType.THREE_ONE));
        mCollageFrameList.add(new CollageFrameModel("Four", R.drawable.collage3_2, R.drawable.collage3_2,CollageFrameType.THREE_TWO));
        mCollageFrameList.add(new CollageFrameModel("Five", R.drawable.collage3_3, R.drawable.collage3_3, CollageFrameType.THREE_THREE));
        mCollageFrameList.add(new CollageFrameModel("Six", R.drawable.collage4_1, R.drawable.collage4_1, CollageFrameType.FOUR_ONE));
        mCollageFrameList.add(new CollageFrameModel("Seven", R.drawable.collage4_2, R.drawable.collage4_2, CollageFrameType.FOUR_TWO));
        mCollageFrameList.add(new CollageFrameModel("Eight", R.drawable.collage4_3, R.drawable.collage4_3, CollageFrameType.FOUR_THREE));
        mCollageFrameList.add(new CollageFrameModel("Nine", R.drawable.collage4_4, R.drawable.collage4_4, CollageFrameType.FOUR_FOUR));
        mCollageFrameList.add(new CollageFrameModel("Ten", R.drawable.collage6_1, R.drawable.collage6_1, CollageFrameType.SIX_ONE));


    }




    @NonNull
    @Override
    public CollageRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


         View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_collage_filter, viewGroup, false);
       // View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collage_frame_itemview, viewGroup, false);
        return new CollageRecycleAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CollageRecycleAdapter.ViewHolder viewHolder, final int position) {

        CollageFrameModel item = mCollageFrameList.get(position);

        viewHolder.mCollageFrameItemImage.setImageResource(item.mCollageFrameIdSmall);
        viewHolder.mCollageFrameItemText.setText(item.mCollageName);



    }

    @Override
    public int getItemCount() {

        return(null!=mCollageFrameList? mCollageFrameList.size():0);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public  View mItemView;

         public LinearLayout mCollageFrameItemLLayout;
        public ImageView mCollageFrameItemImage;
        public TextView mCollageFrameItemText;

        ViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;

            mCollageFrameItemImage =(ImageView)itemView.findViewById(R.id.idCollageItemViewImage);
            mCollageFrameItemText = (TextView)itemView.findViewById(R.id.idCollageItemViewText);

//            mCollageFrameItemLLayout = (LinearLayout) itemView.findViewById(R.id.idCollageFrameItemLLayout);
//            mCollageFrameItemImage =(ImageView)itemView.findViewById(R.id.idCollageFrameItemImage);
//            mCollageFrameItemText = (TextView)itemView.findViewById(R.id.idCollageFrameItemText);



             itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCollageFrameSelected.onCollageFrameSelected(mCollageFrameList.get(getLayoutPosition()).getmCollageFrameIdBig(), mCollageFrameList.get(getLayoutPosition()).getmCollageFrameType());

                }
            });


        }
    }


}

