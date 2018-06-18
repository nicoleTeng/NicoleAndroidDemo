package com.example.draw;

import android.graphics.Path;

public class DrawPathUnit {
    private Path mPath;
    private int mPathWidth;
    private int mPathColor;
    
    public DrawPathUnit(Path path, int pathWidth, int color) {
        mPath = path;
        mPathWidth = pathWidth;
        mPathColor = color;
    }

    public DrawPathUnit(Path path, int pathWidth) {
        mPath = path;
        mPathWidth = pathWidth;
    }
    
    public Path getPath() {
        return mPath;
    }
    
    public int getPathWidth() {
        return mPathWidth;
    }
    
    public int getPathColor() {
        return mPathColor;
    }
}
