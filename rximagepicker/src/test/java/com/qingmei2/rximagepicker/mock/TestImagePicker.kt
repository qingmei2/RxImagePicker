package com.qingmei2.rximagepicker.mock

import android.graphics.Bitmap
import android.net.Uri
import com.qingmei2.rximagepicker.config.observeras.AsBitmap
import com.qingmei2.rximagepicker.config.observeras.AsUri
import com.qingmei2.rximagepicker.config.sources.Camera
import com.qingmei2.rximagepicker.config.sources.Gallery
import io.reactivex.Observable

interface TestImagePicker {

    @Camera
    @AsUri
    fun camera(): Observable<Uri>

    @Gallery
    @AsBitmap
    fun gallery(): Observable<Bitmap>
}
