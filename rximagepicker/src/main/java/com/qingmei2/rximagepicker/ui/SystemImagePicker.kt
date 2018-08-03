package com.qingmei2.rximagepicker.ui

import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import com.qingmei2.rximagepicker.ui.gallery.SystemGalleryPickerView

import io.reactivex.Observable

interface SystemImagePicker {

    @Gallery
    fun openGallery(): Observable<Result>

    @Camera
    fun openCamera(): Observable<Result>

}