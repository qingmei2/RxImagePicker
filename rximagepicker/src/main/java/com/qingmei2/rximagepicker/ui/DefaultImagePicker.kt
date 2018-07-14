package com.qingmei2.rximagepicker.ui


import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery

import io.reactivex.Observable

interface DefaultImagePicker {

    @Gallery
    fun openGallery(): Observable<Result>

    @Camera
    fun openCamera(): Observable<Result>

    companion object {
        const val DEFAULT_PICKER_GALLERY = "com.qingmei2.rximagepicker.pickerview.default.gallery"
        const val DEFAULT_PICKER_CAMERA = "com.qingmei2.rximagepicker.pickerview.default.camera"
    }
}
