package com.qingmei2.rximagepicker.core;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;

import com.qingmei2.rximagepicker.config.RxImagePickerConfigProvider;
import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.funtions.FuntionObserverAsConverter;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * {@link RxImagePickerProcessor} is the class that processing reactive data stream.
 */
public final class RxImagePickerProcessor implements
        IRxImagePickerProcessor {

    @VisibleForTesting
    public final Context context;
    @VisibleForTesting
    public final ICameraPickerView cameraPickerView;
    @VisibleForTesting
    public final IGalleryPickerView galleryPickerView;
    @VisibleForTesting
    public final IRxImagePickerSchedulers schedulers;

    public RxImagePickerProcessor(Context context,
                                  ICameraPickerView cameraPickerView,
                                  IGalleryPickerView galleryPickerView,
                                  IRxImagePickerSchedulers schedulers) {
        this.context = context;
        this.cameraPickerView = cameraPickerView;
        this.galleryPickerView = galleryPickerView;
        this.schedulers = schedulers;
    }

    @Override
    public Observable<?> process(RxImagePickerConfigProvider configProvider) {
        return Observable.just(configProvider)
                .flatMap(sourceFrom(cameraPickerView, galleryPickerView))
                .flatMap(observerAs(configProvider, context))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    @VisibleForTesting
    public Function<RxImagePickerConfigProvider, ObservableSource<Uri>> sourceFrom(
            ICameraPickerView cameraPickerView,
            IGalleryPickerView galleryPickerView) {
        return new Function<RxImagePickerConfigProvider, ObservableSource<Uri>>() {
            @Override
            public ObservableSource<Uri> apply(RxImagePickerConfigProvider provider) throws Exception {
                switch (provider.getSourcesFrom()) {
                    case GALLERY:
                        return galleryPickerView.pickImage();
                    case CAMERA:
                        return cameraPickerView.takePhoto();
                    default:
                        throw new IllegalArgumentException("unknown SourceFrom data.");
                }
            }
        };
    }

    @VisibleForTesting
    public Function<Uri, ObservableSource<?>> observerAs(
            RxImagePickerConfigProvider configProvider,
            Context context) {
        return new FuntionObserverAsConverter(configProvider.getObserverAs(), context);
    }
}
