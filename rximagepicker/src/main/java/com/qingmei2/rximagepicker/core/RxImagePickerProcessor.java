package com.qingmei2.rximagepicker.core;


import android.net.Uri;

import com.qingmei2.rximagepicker.config.RxImagePickerConfigProvider;
import com.qingmei2.rximagepicker.di.scheduler.IRxImagePickerSchedulers;
import com.qingmei2.rximagepicker.funtions.FuntionObserverAsConverter;

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

    private final RxImagePicker imagePicker;
    private final IRxImagePickerSchedulers schedulers;

    public RxImagePickerProcessor(RxImagePicker imagePicker,
                                  IRxImagePickerSchedulers schedulers) {
        this.imagePicker = imagePicker;
        this.schedulers = schedulers;
    }

    @Override
    public Observable<?> process(RxImagePickerConfigProvider configProvider) {
        return Observable.just(configProvider)
                .flatMap(new Function<RxImagePickerConfigProvider, ObservableSource<Uri>>() {
                    @Override
                    public ObservableSource<Uri> apply(RxImagePickerConfigProvider provider) throws Exception {
                        return imagePicker.requestImage(provider.getSourcesFrom());
                    }
                })
                .flatMap(new FuntionObserverAsConverter(configProvider.getObserverAs(), imagePicker.getActivity()))
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui());
    }
}
