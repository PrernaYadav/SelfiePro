package com.mojodigi.selfiepro.model;

import com.mojodigi.selfiepro.enums.CollageFrameType;
import com.mojodigi.selfiepro.enums.ToolType;

public class CollageFrameModel {

    public String mCollageName;
    public int mCollageFrameIdSmall;
    public int mCollageFrameIdBig;
    public CollageFrameType mCollageFrameType;

    public CollageFrameModel(String mCollageName, int mCollageFrameIdSmall, int mCollageFrameIdBig, CollageFrameType mCollageFrameType) {
        this.mCollageName = mCollageName;
        this.mCollageFrameIdSmall = mCollageFrameIdSmall;
        this.mCollageFrameIdBig = mCollageFrameIdBig;
        this.mCollageFrameType = mCollageFrameType;
    }

    public String getmCollageName() {
        return mCollageName;
    }

    public void setmCollageName(String mCollageName) {
        this.mCollageName = mCollageName;
    }

    public int getmCollageFrameIdSmall() {
        return mCollageFrameIdSmall;
    }

    public void setmCollageFrameIdSmall(int mCollageFrameIdSmall) {
        this.mCollageFrameIdSmall = mCollageFrameIdSmall;
    }

    public int getmCollageFrameIdBig() {
        return mCollageFrameIdBig;
    }

    public void setmCollageFrameIdBig(int mCollageFrameIdBig) {
        this.mCollageFrameIdBig = mCollageFrameIdBig;
    }

    public CollageFrameType getmCollageFrameType() {
        return mCollageFrameType;
    }

    public void setmCollageFrameType(CollageFrameType mCollageFrameType) {
        this.mCollageFrameType = mCollageFrameType;
    }
}
