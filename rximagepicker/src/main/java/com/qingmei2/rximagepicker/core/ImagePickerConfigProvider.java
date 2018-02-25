package com.qingmei2.rximagepicker.core;

import android.support.annotation.IdRes;

import com.qingmei2.rximagepicker.config.observeras.ObserverAs;
import com.qingmei2.rximagepicker.config.sources.SourcesFrom;
import com.qingmei2.rximagepicker.ui.IPickerView;

/**
 * Entity class for user config.
 */
public final class ImagePickerConfigProvider {

    private final SourcesFrom sourcesFrom;
    private final ObserverAs observerAs;
    private final IPickerView pickerView;
    private final int containerViewId;
    private final String pickViewTag;

    public ImagePickerConfigProvider(SourcesFrom sourcesFrom,
                                     ObserverAs observerAs,
                                     IPickerView pickerView,
                                     @IdRes int containerViewId,
                                     String pickViewTag) {
        this.sourcesFrom = sourcesFrom;
        this.observerAs = observerAs;
        this.pickerView = pickerView;
        this.containerViewId = containerViewId;
        this.pickViewTag = pickViewTag;
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

    public String getPickViewTag() {
        return pickViewTag;
    }
}
