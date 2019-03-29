package com.darekeapp.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {

    private List<String> oldList;
    private List<String> newList;

    public DiffUtilCallback(List<String> oldList, List<String> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }


    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldPosition, int newPosition) {
        return oldPosition == newPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldPosition, int newPosition) {
        return oldList.get(oldPosition) == newList.get(newPosition);
    }
}
