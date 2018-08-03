package com.qingmei2.rximagepicker.core

import com.qingmei2.rximagepicker.providers.ConfigProvider
import com.qingmei2.rximagepicker.scheduler.IRxImagePickerSchedulers
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import com.qingmei2.rximagepicker.providers.RuntimeProvider
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import io.reactivex.Observable

/**
 * [ImagePickerConfigProcessor] is the class that processing reactive data stream.
 */
class ImagePickerConfigProcessor(private val schedulers: IRxImagePickerSchedulers)
    : IImagePickerProcessor {

    override fun process(configProvider: ConfigProvider,
                         runtimeProvider: RuntimeProvider): Observable<*> {
        return Observable.just(0)
                .flatMap {
                    if (configProvider.asFragment) {
                        return@flatMap ActivityPickerViewController.instance.pickImage()
                    }
                    when (configProvider.sourcesFrom) {
                        SourcesFrom.GALLERY,
                        SourcesFrom.CAMERA -> runtimeProvider.pickerView.pickImage()
                    }
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }
}