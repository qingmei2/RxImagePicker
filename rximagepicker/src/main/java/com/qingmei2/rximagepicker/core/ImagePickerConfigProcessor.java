package com.qingmei2.rximagepicker.core;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;

import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.funtions.ObserverAsConverter;
import com.qingmei2.rximagepicker.ui.ICameraPickerView;
import com.qingmei2.rximagepicker.ui.IGalleryPickerView;

import java.util.Map;

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
    public final Map<String, ICameraPickerView> cameraViews;
    @VisibleForTesting
    public final Map<String, IGalleryPickerView> galleryViews;
    @VisibleForTesting
    public final IRxImagePickerSchedulers schedulers;

    public ImagePickerConfigProcessor(Context context,
                                      Map<String, ICameraPickerView> cameraViews,
                                      Map<String, IGalleryPickerView> galleryViews,
                                      IRxImagePickerSchedulers schedulers) {
        this.context = context;
        this.cameraViews = cameraViews;
        this.galleryViews = galleryViews;
        this.schedulers = schedulers;
    }

    @Override
    public Observable<?> process(ImagePickerConfigProvider configProvider) {
        return Observable.just(configProvider)
                .flatMap(sourceFrom(cameraViews, galleryViews))
                .flatMap(observerAs(configProvider, context))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }

    @VisibleForTesting
    public Function<ImagePickerConfigProvider, ObservableSource<Uri>> sourceFrom(
            Map<String, ICameraPickerView> cameraViews,
            Map<String, IGalleryPickerView> galleryViews) {
        return new Function<ImagePickerConfigProvider, ObservableSource<Uri>>() {
            @Override
            public ObservableSource<Uri> apply(ImagePickerConfigProvider provider) throws Exception {
                switch (provider.getSourcesFrom()) {
                    case GALLERY:
                    case CAMERA:
                        return provider.getPickerView().pickImage();
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
