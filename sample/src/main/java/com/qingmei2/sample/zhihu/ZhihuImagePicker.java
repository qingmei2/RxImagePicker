package com.qingmei2.sample.zhihu;

import android.net.Uri;

import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface ZhihuImagePicker {

    String KEY_ZHIHU_PICKER_NORMAL = "key_zhihu_picker_theme_normal";
    String KEY_ZHIHU_PICKER_DRACULA = "key_zhihu_picker_theme_dracula";

    @Gallery(viewKey = KEY_ZHIHU_PICKER_NORMAL)
    Observable<Uri> openGalleryAsNormal();

    @Gallery(viewKey = KEY_ZHIHU_PICKER_DRACULA)
    Observable<Uri> openGalleryAsDracula();

    @Camera
    Observable<Uri> openCamera();
}
