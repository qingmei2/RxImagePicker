package com.qingmei2.rximagepicker.core;

import android.net.Uri;

import io.reactivex.Observable;


/**
 * Created by qingmei2 on 2018/1/13.
 */
public interface IRxImagePickerProcessor {

    Observable<Uri> process(final RxImagePickerConfigProvider configProvider);
}
