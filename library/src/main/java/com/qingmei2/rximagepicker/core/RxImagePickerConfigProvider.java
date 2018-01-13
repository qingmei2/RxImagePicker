package com.qingmei2.rximagepicker.core;

import com.qingmei2.rximagepicker.mode.Sources;

/**
 * Entity class for user config
 * <p>
 * Created by qingmei2 on 2018/1/13.
 */
public final class RxImagePickerConfigProvider {

    private final Sources imagePickerMode;

    public RxImagePickerConfigProvider(Sources imagePickerMode) {
        this.imagePickerMode = imagePickerMode;
    }

    public Sources getImagePickerMode() {
        return imagePickerMode;
    }
}
