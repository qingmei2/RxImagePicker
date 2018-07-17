package com.qingmei2.sample.zhihu

import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery

import io.reactivex.Observable

interface ZhihuImagePicker {

    @Gallery(viewKey = KEY_ZHIHU_PICKER_NORMAL)
    fun openGalleryAsNormal(): Observable<Result>

    @Gallery(viewKey = KEY_ZHIHU_PICKER_DRACULA)
    fun openGalleryAsDracula(): Observable<Result>

    @Camera
    fun openCamera(): Observable<Result>

    companion object {

        const val KEY_ZHIHU_PICKER_NORMAL = "key_zhihu_picker_theme_normal"
        const val KEY_ZHIHU_PICKER_DRACULA = "key_zhihu_picker_theme_dracula"
    }
}
