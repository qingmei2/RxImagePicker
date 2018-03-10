package com.qingmei2.sample;

import android.graphics.Bitmap;

import com.qingmei2.rximagepicker.entity.observeras.AsBitmap;
import com.qingmei2.rximagepicker.entity.observeras.AsFile;
import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface MyImagePicker {

    String KEY_ZHIHU_PICKER_ACTIVITY = "key_zhihu_picker_as_activity";
    String KEY_ZHIHU_PICKER_FRAGMENT = "key_zhihu_picker_as_fragment";

    @AsBitmap
    @Gallery(viewKey = KEY_ZHIHU_PICKER_ACTIVITY)
    Observable<Bitmap> openGallery();

    @AsBitmap
    @Gallery(viewKey = KEY_ZHIHU_PICKER_FRAGMENT, containerViewId = R.id.fl_container)
    Observable<Bitmap> openGalleryWithFragment();

    @AsFile
    @Camera
    Single<File> openCamera();
}
