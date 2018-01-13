package com.qingmei2.sample;

import android.net.Uri;

import com.qingmei2.rximagepicker.mode.Camera;
import com.qingmei2.rximagepicker.mode.Gallery;

import io.reactivex.Observable;

/**
 * Created by qingmei2 on 2018/1/13.
 */
public interface IRxImagePicker {

    @Gallery
    Observable<Uri> openGallery();

    @Camera
    Observable<Uri> openCamera();
}
