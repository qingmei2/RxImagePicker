package com.qingmei2.sample.zhihu;

import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

import io.reactivex.Observable;

public interface ZhihuImagePicker {

    String KEY_ZHIHU_PICKER_NORMAL = "key_zhihu_picker_theme_normal";
    String KEY_ZHIHU_PICKER_DRACULA = "key_zhihu_picker_theme_dracula";

    @Gallery(viewKey = KEY_ZHIHU_PICKER_NORMAL)
    Observable<Result> openGalleryAsNormal();

    @Gallery(viewKey = KEY_ZHIHU_PICKER_DRACULA)
    Observable<Result> openGalleryAsDracula();

    @Camera
    Observable<Result> openCamera();
}
