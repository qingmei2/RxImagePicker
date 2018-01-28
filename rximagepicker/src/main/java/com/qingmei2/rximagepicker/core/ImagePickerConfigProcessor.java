package com.qingmei2.rximagepicker.core;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;

import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.funtions.ObserverAsConverter;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * {@link ImagePickerConfigProcessor} is the class that processing reactive data stream.
 */
public final class ImagePickerConfigProcessor implements
        IImagePickerProcessor {

    @VisibleForTesting
    public final Context context;
    @VisibleForTesting
    public final ICameraPickerView cameraPickerView;
    @VisibleForTesting
    public final IGalleryPickerView galleryPickerView;
    @VisibleForTesting
    public final IRxImagePickerSchedulers schedulers;

    public ImagePickerConfigProcessor(Context context,
                                      ICameraPickerView cameraPickerView,
                                      IGalleryPickerView galleryPickerView,
                                      IRxImagePickerSchedulers schedulers) {
        this.context = context;
        this.cameraPickerView = cameraPickerView;
        this.galleryPickerView = galleryPickerView;
        this.schedulers = schedulers;
    }

    @Override
    public Observable<?> process(ImagePickerConfigProvider configProvider) {
        return Observable.just(configProvider)
                .flatMap(sourceFrom(cameraPickerView, galleryPickerView))
                .flatMap(observerAs(configProvider, context))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    @VisibleForTesting
    public Function<ImagePickerConfigProvider, ObservableSource<Uri>> sourceFrom(
            ICameraPickerView cameraPickerView,
            IGalleryPickerView galleryPickerView) {
        return new Function<ImagePickerConfigProvider, ObservableSource<Uri>>() {
            @Override
            public ObservableSource<Uri> apply(ImagePickerConfigProvider provider) throws Exception {
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
            ImagePickerConfigProvider configProvider,
            Context context) {
        return new ObserverAsConverter(configProvider.getObserverAs(), context);
    }
}
