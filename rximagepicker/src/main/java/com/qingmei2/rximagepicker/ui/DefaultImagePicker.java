package com.qingmei2.rximagepicker.ui;


import android.net.Uri;

import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

import io.reactivex.Observable;

public interface DefaultImagePicker {

    String DEFAULT_PICKER_GALLERY = "com.qingmei2.rximagepicker.pickerview.default.gallery";
    String DEFAULT_PICKER_CAMERA = "com.qingmei2.rximagepicker.pickerview.default.camera";

    @Gallery
    Observable<Uri> openGallery();

    @Camera
    Observable<Uri> openCamera();
}
