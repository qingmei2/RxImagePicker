package com.qingmei2.rximagepicker.core;

import com.qingmei2.rximagepicker.config.RxImagePickerConfigProvider;

import io.reactivex.Observable;

public interface IRxImagePickerProcessor {

    Observable<?> process(final RxImagePickerConfigProvider configProvider);
}
