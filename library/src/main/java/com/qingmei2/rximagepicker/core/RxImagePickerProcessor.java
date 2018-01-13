package com.qingmei2.rximagepicker.core;


import android.net.Uri;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by qingmei2 on 2018/1/13.
 */
public final class RxImagePickerProcessor implements IRxImagePickerProcessor {

    private final RxImagePicker2 rxImagePicker2;

    @Inject
    public RxImagePickerProcessor(RxImagePicker2 rxImagePicker2) {
        this.rxImagePicker2 = rxImagePicker2;
    }

    @Override
    public Observable<Uri> process(RxImagePickerConfigProvider configProvider) {
        return Observable.just(configProvider.getImagePickerMode())
                .flatMap(rxImagePicker2::requestImage);
    }

}
