package com.mojodigi.selfiepro.model;

import com.mojodigi.selfiepro.enums.ToolType;

public class ToolModel {

    public String mToolName;
    public int mToolIcon;
    public ToolType mToolType;

      public ToolModel(String toolName, int toolIcon, ToolType toolType) {
            mToolName = toolName;
            mToolIcon = toolIcon;
            mToolType = toolType;
        }
}
