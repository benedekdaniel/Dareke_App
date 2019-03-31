package com.darekeapp.utils;

import android.support.v7.util.DiffUtil;

import com.darekeapp.database.ShiftLog;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {
    private List<ShiftLog> oldList;
    private List<ShiftLog> newList;

    public DiffUtilCallback(List<ShiftLog> oldList, List<ShiftLog> newList) {
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
        return newList.get(newPosition) == oldList.get(oldPosition);
    }
}
