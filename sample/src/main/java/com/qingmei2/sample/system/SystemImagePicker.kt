package com.qingmei2.sample.system

import android.content.Context
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import io.reactivex.Observable

interface SystemImagePicker {

    @Gallery
    fun openGallery(context: Context,
                    config: ICustomPickerConfiguration?): Observable<Result>

    @Camera
    fun openCamera(context: Context): Observable<Result>
}