package com.qingmei2.rximagepicker.core;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.ui.IPickerView;
import com.qingmei2.rximagepicker.ui.ImagePickerHolderActivity;

import static com.qingmei2.rximagepicker.ui.ImagePickerHolderActivity.EXTRA_PICKER_VIEW;

public class ImagePickerProjector {

    private IPickerView pickerView;

    private final boolean singleActivity;
    private FragmentActivity fragmentActivity;
    private int containerViewId;
    private String tag;

    public ImagePickerProjector(IPickerView pickerView,
                                FragmentActivity fragmentActivity,
                                @IdRes int containerViewId,
                                String tag,
                                boolean singleActivity) {
        this.pickerView = pickerView;
        this.fragmentActivity = fragmentActivity;
        this.containerViewId = containerViewId;
        this.tag = tag;
        this.singleActivity = singleActivity;
    }

    public void display() {
        if (singleActivity)
            displayPickerViewAsActivity();
        else
            displayPickerViewAsFragment();
    }

    private void displayPickerViewAsActivity() {
        fragmentActivity.startActivity(new Intent(fragmentActivity, ImagePickerHolderActivity.class)
                .putExtra(EXTRA_PICKER_VIEW, pickerView));
    }

    private void displayPickerViewAsFragment() {
        pickerView.display(fragmentActivity.getSupportFragmentManager(), containerViewId, tag);
    }
}
