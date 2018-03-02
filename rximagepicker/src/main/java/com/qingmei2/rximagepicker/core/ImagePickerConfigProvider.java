package com.qingmei2.rximagepicker.core;

import android.app.Activity;
import android.support.annotation.IdRes;

import com.qingmei2.rximagepicker.config.observeras.ObserverAs;
import com.qingmei2.rximagepicker.config.sources.SourcesFrom;
import com.qingmei2.rximagepicker.ui.IPickerView;

/**
 * Entity class for user config.
 */
public final class ImagePickerConfigProvider {

    private final String viewKey;
    private final boolean singleActivity;

    private final SourcesFrom sourcesFrom;
    private final ObserverAs observerAs;

    private final int containerViewId;
    private final IPickerView pickerView;

    private final Class<? extends Activity> activityClass;

    public ImagePickerConfigProvider(boolean singleActivity,
                                     String viewKey,
                                     SourcesFrom sourcesFrom,
                                     ObserverAs observerAs,
                                     IPickerView pickerView,
                                     @IdRes int containerViewId,
                                     Class<? extends Activity> activityClass
    ) {
        this.sourcesFrom = sourcesFrom;
        this.observerAs = observerAs;
        this.pickerView = pickerView;
        this.containerViewId = containerViewId;
        this.viewKey = viewKey;
        this.singleActivity = singleActivity;
        this.activityClass = activityClass;
    }

    public SourcesFrom getSourcesFrom() {
        return sourcesFrom;
    }

    public ObserverAs getObserverAs() {
        return observerAs;
    }

    public IPickerView getPickerView() {
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
