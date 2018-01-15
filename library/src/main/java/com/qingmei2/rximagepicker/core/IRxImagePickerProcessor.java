package com.qingmei2.rximagepicker.core;

import com.qingmei2.rximagepicker.config.RxImagePickerConfigProvider;

import io.reactivex.Observable;


/**
 * Created by qingmei2 on 2018/1/13.
 */
public interface IRxImagePickerProcessor {

    Observable<?> process(final RxImagePickerConfigProvider configProvider);
}
