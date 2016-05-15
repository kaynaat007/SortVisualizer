package com.example.root.sortvisualizer;

/**
 * Created by root on 15/5/16.
 */
public class HeapSortModel {
    int mNumber,color;

    public HeapSortModel(int mNumber,int color) {
        this.color = color;
        this.mNumber = mNumber;
    }

    public int getmNumber() {
        return mNumber;
    }

    public void setmNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
