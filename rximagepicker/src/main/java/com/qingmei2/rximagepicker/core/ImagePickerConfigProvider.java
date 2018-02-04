package com.qingmei2.rximagepicker.core;

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

    public ImagePickerConfigProvider(SourcesFrom sourcesFrom,
                                     ObserverAs observerAs,
                                     IPickerView pickerView) {
        this.sourcesFrom = sourcesFrom;
        this.observerAs = observerAs;
        this.pickerView = pickerView;
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
}
