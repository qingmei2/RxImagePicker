package com.qingmei2.rximagepicker.core;


import android.net.Uri;

import com.qingmei2.rximagepicker.config.observeras.AsUri;
import com.qingmei2.rximagepicker.config.sources.Camera;
import com.qingmei2.rximagepicker.config.sources.Gallery;

import io.reactivex.Observable;

public interface DefaultImagePicker {

    String DEFAULT_PICKER_GALLERY = "com.qingmei2.rximagepicker.pickerview.default.gallery";
    String DEFAULT_PICKER_CAMERA = "com.qingmei2.rximagepicker.pickerview.default.camera";

    @Gallery
    @AsUri
    Observable<Uri> openGallery();

    @Camera
    @AsUri
    Observable<Uri> openCamera();
}
