package com.qingmei2.rximagepicker.core;

import io.reactivex.Observable;

public interface IImagePickerProcessor {

    Observable<?> process(final ImagePickerConfigProvider configProvider);
}
