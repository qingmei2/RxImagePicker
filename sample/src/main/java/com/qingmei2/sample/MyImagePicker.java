package com.qingmei2.sample;

import android.graphics.Bitmap;

import com.qingmei2.rximagepicker.config.observeras.AsBitmap;
import com.qingmei2.rximagepicker.config.observeras.AsFile;
import com.qingmei2.rximagepicker.config.sources.Camera;
import com.qingmei2.rximagepicker.config.sources.Gallery;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface MyImagePicker {

    String KEY_WECHAT_PICKER_ACTIVITY = "key_wechat_picker_activity";
    String KEY_WECHAT_PICKER_FRAGMENT = "key_wechat_picker_fragment";

    @AsBitmap
    @Gallery(viewKey = KEY_WECHAT_PICKER_ACTIVITY)
    Observable<Bitmap> openGallery();

    @AsBitmap
    @Gallery(viewKey = KEY_WECHAT_PICKER_FRAGMENT, containerViewId = R.id.fl_container)
    Observable<Bitmap> openGalleryWithFragment();

    @AsFile
    @Camera
    Single<File> openCamera();
}
