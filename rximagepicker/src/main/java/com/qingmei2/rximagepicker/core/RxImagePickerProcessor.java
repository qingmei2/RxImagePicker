package com.qingmei2.rximagepicker.core;


import android.content.Context;
import android.net.Uri;

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
 * <p>
 * Created by qingmei2 on 2018/1/13.
 */
public final class RxImagePickerProcessor implements
        IRxImagePickerProcessor {

    private final Context context;
    private final ICameraPickerView cameraPickerView;
    private final IGalleryPickerView galleryPickerView;
    private final IRxImagePickerSchedulers schedulers;

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
                .flatMap(new Function<RxImagePickerConfigProvider, ObservableSource<Uri>>() {
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
                })
                .flatMap(new FuntionObserverAsConverter(configProvider.getObserverAs(), context))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }
}
