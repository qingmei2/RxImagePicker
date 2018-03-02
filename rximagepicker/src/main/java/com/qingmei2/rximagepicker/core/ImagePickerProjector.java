package com.qingmei2.rximagepicker.core;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;

import com.qingmei2.rximagepicker.ui.HolderActivity;
import com.qingmei2.rximagepicker.ui.IPickerView;

public class ImagePickerProjector {

    private IPickerView pickerView;

    private final boolean singleActivity;
    private final Class<? extends Activity> activityClass;
    private FragmentActivity fragmentActivity;
    private int containerViewId;
    private String viewKey;

    public ImagePickerProjector(boolean singleActivity,
                                String viewKey,
                                IPickerView pickerView,
                                FragmentActivity fragmentActivity,
                                @IdRes int containerViewId,
                                Class<? extends Activity> activityClass) {
        this.pickerView = pickerView;
        this.fragmentActivity = fragmentActivity;
        this.containerViewId = containerViewId;
        this.viewKey = viewKey;
        this.singleActivity = singleActivity;
        this.activityClass = activityClass;
    }

    public void display() {
        if (singleActivity)
            displayPickerViewAsActivity();
        else
            displayPickerViewAsFragment();
    }

    private void displayPickerViewAsActivity() {
        HolderActivity.setActivityClass(activityClass);
        fragmentActivity.startActivity(new Intent(fragmentActivity, HolderActivity.class));
    }

    private void displayPickerViewAsFragment() {
        pickerView.display(fragmentActivity.getSupportFragmentManager(), containerViewId, viewKey);
    }
}
