package com.qingmei2.rximagepicker.core;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import com.qingmei2.rximagepicker.ui.IPickerView;

public class ImagePickerProjector {

    private IPickerView pickerView;

    private FragmentManager fragmentManager;
    private int containerViewId;
    private String tag;

    public ImagePickerProjector(IPickerView pickerView,
                                FragmentManager fragmentManager,
                                @IdRes int containerViewId,
                                String tag) {
        this.pickerView = pickerView;
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
        this.tag = tag;
    }

    public void displayPickerView() {
        pickerView.display(fragmentManager, containerViewId, tag);
    }
}
