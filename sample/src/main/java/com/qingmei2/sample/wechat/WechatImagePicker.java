package com.qingmei2.sample.wechat;

import android.graphics.Bitmap;

import com.qingmei2.rximagepicker.entity.observeras.AsBitmap;
import com.qingmei2.rximagepicker.entity.observeras.AsFile;
import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface WechatImagePicker {

    String KEY_WECHAT_PICKER_ACTIVITY = "key_wechat_picker";

    @AsBitmap
    @Gallery(viewKey = KEY_WECHAT_PICKER_ACTIVITY)
    Observable<Bitmap> openGallery();

    @AsFile
    @Camera
    Single<File> openCamera();
}
