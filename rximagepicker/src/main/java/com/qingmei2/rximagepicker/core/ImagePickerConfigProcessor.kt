package com.qingmei2.rximagepicker.core


import android.content.Context
import android.support.annotation.VisibleForTesting

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
class ImagePickerConfigProcessor(@field:VisibleForTesting
                                 val context: Context,
                                 @field:VisibleForTesting
                                 val cameraViews: Map<String, ICameraCustomPickerView>,
                                 @field:VisibleForTesting
                                 val galleryViews: Map<String, IGalleryCustomPickerView>,
                                 @field:VisibleForTesting
                                 val schedulers: IRxImagePickerSchedulers) : IImagePickerProcessor {

    override fun process(configProvider: ImagePickerConfigProvider): Observable<*> {
        return Observable.just(configProvider)
                .flatMap(sourceFrom(cameraViews, galleryViews))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
    }

    @VisibleForTesting
    fun sourceFrom(
            cameraViews: Map<String, ICameraCustomPickerView>,
            galleryViews: Map<String, IGalleryCustomPickerView>
    ): Function<ImagePickerConfigProvider, ObservableSource<Result>> {
        return Function { provider ->
            if (provider.isSingleActivity) {
                return@Function ActivityPickerViewController.getInstance().pickImage()
            }
            when (provider.sourcesFrom) {
                SourcesFrom.GALLERY,
                SourcesFrom.CAMERA -> provider.pickerView.pickImage()
                else -> throw IllegalArgumentException("unknown SourceFrom data.")
            }
        }
    }
}
