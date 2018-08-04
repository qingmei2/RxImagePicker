package com.qingmei2.rximagepicker.core

import com.qingmei2.rximagepicker.entity.ConfigProvider
import com.qingmei2.rximagepicker.scheduler.IRxImagePickerSchedulers
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import io.reactivex.Observable

/**
 * [ConfigProcessor] is the class that processing reactive data stream.
 */
class ConfigProcessor(private val schedulers: IRxImagePickerSchedulers) {

    fun process(configProvider: ConfigProvider): Observable<*> {
        return Observable.just(0)
                .flatMap {
                    if (!configProvider.asFragment) {
                        return@flatMap ActivityPickerViewController.instance.pickImage()
                    }
                    when (configProvider.sourcesFrom) {
                        SourcesFrom.GALLERY,
                        SourcesFrom.CAMERA -> configProvider.pickerView.pickImage()
                    }
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }
}