package com.qingmei2.rximagepicker.config;

import com.qingmei2.rximagepicker.config.observeras.ObserverAs;
import com.qingmei2.rximagepicker.config.sources.SourcesFrom;

/**
 * Entity class for user config
 * <p>
 * Created by qingmei2 on 2018/1/13.
 */
public final class RxImagePickerConfigProvider {

    private final SourcesFrom sourcesFrom;
    private final ObserverAs observerAs;

    public RxImagePickerConfigProvider(SourcesFrom sourcesFrom, ObserverAs observerAs) {
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
