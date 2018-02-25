package com.qingmei2.sample;

import android.graphics.Bitmap;

import com.qingmei2.rximagepicker.config.observeras.AsBitmap;
import com.qingmei2.rximagepicker.config.observeras.AsFile;
import com.qingmei2.rximagepicker.config.sources.Camera;
import com.qingmei2.rximagepicker.config.sources.Gallery;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface MyImagePicker {

    @Gallery
    @AsBitmap
    Observable<Bitmap> openGallery();

    @Camera
    @AsFile
    Single<File> openCamera();
}
