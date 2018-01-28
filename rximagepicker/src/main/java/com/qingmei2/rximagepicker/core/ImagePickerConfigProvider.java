package com.qingmei2.rximagepicker.core;

import com.qingmei2.rximagepicker.config.observeras.ObserverAs;
import com.qingmei2.rximagepicker.config.sources.SourcesFrom;

/**
 * Entity class for user config.
 */
public final class ImagePickerConfigProvider {

    private final SourcesFrom sourcesFrom;
    private final ObserverAs observerAs;

    public ImagePickerConfigProvider(SourcesFrom sourcesFrom, ObserverAs observerAs) {
        this.sourcesFrom = sourcesFrom;
        this.observerAs = observerAs;
    }

    public SourcesFrom getSourcesFrom() {
        return sourcesFrom;
    }

    public ObserverAs getObserverAs() {
        return observerAs;
    }
}
