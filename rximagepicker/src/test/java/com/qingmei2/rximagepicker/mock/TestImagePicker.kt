package com.qingmei2.rximagepicker.mock

import android.graphics.Bitmap
import android.net.Uri
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

interface TestImagePicker {

    @Camera
    fun camera(): Observable<Uri>

    @Gallery
    fun gallery(): Observable<Uri>

    @Gallery
    fun flowableGalleryReturnBitmap(): Flowable<Uri>

    @Camera
    fun SingleCameraReturnFile(): Single<Uri>

    @Camera
    fun MaybeCameraReturnUri(): Maybe<Uri>
}
