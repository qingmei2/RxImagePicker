package com.qingmei2.rximagepicker.core

import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.SourcesFrom
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * [ImagePickerConfigProcessor] is the class that processing reactive data stream.
 */
class ImagePickerConfigProcessor(private val cameraViews: Map<String, ICameraCustomPickerView>,
                                 private val galleryViews: Map<String, IGalleryCustomPickerView>,
                                 private val schedulers: IRxImagePickerSchedulers)
    : IImagePickerProcessor {

    override fun process(configProvider: ImagePickerConfigProvider): Observable<*> {
        return Observable.just(configProvider)
                .flatMap(sourceFrom())
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    private fun sourceFrom(): Function<ImagePickerConfigProvider, ObservableSource<Result>> {
        return Function { provider ->
            if (provider.isSingleActivity) {
                return@Function ActivityPickerViewController.instance.pickImage()
            }
            when (provider.sourcesFrom) {
                SourcesFrom.GALLERY,
                SourcesFrom.CAMERA -> provider.pickerView!!.pickImage()
            }
        }
    }
}