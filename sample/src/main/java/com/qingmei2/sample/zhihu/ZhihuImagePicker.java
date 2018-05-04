package com.qingmei2.sample.zhihu;

import android.graphics.Bitmap;

import com.qingmei2.rximagepicker.entity.observeras.AsBitmap;
import com.qingmei2.rximagepicker.entity.observeras.AsFile;
import com.qingmei2.rximagepicker.entity.sources.Camera;
import com.qingmei2.rximagepicker.entity.sources.Gallery;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface ZhihuImagePicker {

    String KEY_ZHIHU_PICKER_NORMAL = "key_zhihu_picker_theme_normal";
    String KEY_ZHIHU_PICKER_DRACULA = "key_zhihu_picker_theme_dracula";

    @AsBitmap
    @Gallery(viewKey = KEY_ZHIHU_PICKER_NORMAL)
    Observable<Bitmap> openGalleryAsNormal();

    @AsBitmap
    @Gallery(viewKey = KEY_ZHIHU_PICKER_DRACULA)
    Observable<Bitmap> openGalleryAsDracula();

    @AsFile
    @Camera
    Single<File> openCamera();
}
