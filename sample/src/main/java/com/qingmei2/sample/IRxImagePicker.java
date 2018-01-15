package com.qingmei2.sample;

import android.graphics.Bitmap;

import com.qingmei2.rximagepicker.config.observeras.AsBitmap;
import com.qingmei2.rximagepicker.config.observeras.AsFile;
import com.qingmei2.rximagepicker.config.sources.Camera;
import com.qingmei2.rximagepicker.config.sources.Gallery;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by qingmei2 on 2018/1/13.
 */
public interface IRxImagePicker {

    @Gallery
    @AsBitmap
    Observable<Bitmap> openGalleryReturnBitmap();

    @Camera
    @AsFile
    Observable<File> openCameraReturnFile();

}
