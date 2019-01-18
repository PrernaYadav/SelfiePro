package com.mojodigi.selfiepro.model;
import com.mojodigi.selfiepro.enums.CollageEditToolsType;


public class CollageEditToolsModel {

    public String mToolName;
    public int mToolIcon;
    public CollageEditToolsType mCollageEditToolsType;

    public CollageEditToolsModel(String toolName, int toolIcon, CollageEditToolsType collageEditToolsType) {
        mToolName = toolName;
        mToolIcon = toolIcon;
        mCollageEditToolsType = collageEditToolsType;
    }

    public String getmToolName() {
        return mToolName;
    }

    public void setmToolName(String mToolName) {
        this.mToolName = mToolName;
    }

    public int getmToolIcon() {
        return mToolIcon;
    }

    public void setmToolIcon(int mToolIcon) {
        this.mToolIcon = mToolIcon;
    }

    public CollageEditToolsType getmCollageEditToolsType() {
        return mCollageEditToolsType;
    }

    public void setmCollageEditToolsType(CollageEditToolsType mCollageEditToolsType) {
        this.mCollageEditToolsType = mCollageEditToolsType;
    }


}
