package com.qingmei2.sample.wechat;

import android.net.Uri;

import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface WechatImagePicker {

    String KEY_WECHAT_PICKER_ACTIVITY = "key_wechat_picker";

    @Gallery(viewKey = KEY_WECHAT_PICKER_ACTIVITY)
    Observable<Uri> openGallery();

    @Camera
    Observable<Uri> openCamera();
}
