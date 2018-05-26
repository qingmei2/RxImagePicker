package com.qingmei2.rximagepicker.core;

import android.app.Activity;
import android.support.annotation.IdRes;

import com.qingmei2.rximagepicker.entity.sources.SourcesFrom;
import com.qingmei2.rximagepicker.ui.ICustomPickerView;

/**
 * Entity class for user config.
 */
public final class ImagePickerConfigProvider {

    private final String viewKey;
    private final boolean singleActivity;

    private final SourcesFrom sourcesFrom;

    private final int containerViewId;
    private final ICustomPickerView pickerView;

    private final Class<? extends Activity> activityClass;

    public ImagePickerConfigProvider(boolean singleActivity,
                                     String viewKey,
                                     SourcesFrom sourcesFrom,
                                     ICustomPickerView pickerView,
                                     @IdRes int containerViewId,
                                     Class<? extends Activity> activityClass
    ) {
        this.sourcesFrom = sourcesFrom;
        this.pickerView = pickerView;
        this.containerViewId = containerViewId;
        this.viewKey = viewKey;
        this.singleActivity = singleActivity;
        this.activityClass = activityClass;
    }

    public SourcesFrom getSourcesFrom() {
        return sourcesFrom;
    }

    public ICustomPickerView getPickerView() {
        return pickerView;
    }

    public int getContainerViewId() {
        return containerViewId;
    }

    public String getViewKey() {
        return viewKey;
    }

    public boolean isSingleActivity() {
        return singleActivity;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }
}
