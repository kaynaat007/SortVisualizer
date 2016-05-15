package com.example.root.sortvisualizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 15/5/16.
 */
public class BucketSortModel {

    private List<Integer> mBucketList = new ArrayList<>();
    private int mSize;

    public List<Integer> getMbucketList() {
        return mBucketList;
    }

    public void setMbucketList(List<Integer> mbucketList) {
        this.mBucketList = mbucketList;
    }

    public int getmSize() {
        return mSize;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }

}
