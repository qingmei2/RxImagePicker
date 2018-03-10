package com.qingmei2.rximagepicker.mock

import android.graphics.Bitmap
import android.net.Uri
import com.qingmei2.rximagepicker.entity.observeras.AsBitmap
import com.qingmei2.rximagepicker.entity.observeras.AsFile
import com.qingmei2.rximagepicker.entity.observeras.AsUri
import com.qingmei2.rximagepicker.entity.sources.Camera
import com.qingmei2.rximagepicker.entity.sources.Gallery
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

interface TestImagePicker {

    @Camera
    @AsUri
    fun camera(): Observable<Uri>

    @Gallery
    @AsBitmap
    fun gallery(): Observable<Bitmap>

    @Gallery
    @AsBitmap
    fun flowableGalleryReturnBitmap(): Flowable<Bitmap>

    @Camera
    @AsFile
    fun SingleCameraReturnFile(): Single<File>

    @Camera
    fun MaybeCameraReturnUri(): Maybe<Uri>
}
