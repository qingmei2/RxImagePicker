package com.qingmei2.sample.wechat;

import android.content.Context;

import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration;
import com.qingmei2.rximagepicker_extension_wechat.ui.WechatImagePickerActivity;

import io.reactivex.rxjava3.core.Observable;

public interface WechatImagePicker {

    @Gallery(componentClazz = WechatImagePickerActivity.class,
            openAsFragment = false)
    Observable<Result> openGallery(Context context, ICustomPickerConfiguration config);

    @Camera
    Observable<Result> openCamera(Context context);
}
