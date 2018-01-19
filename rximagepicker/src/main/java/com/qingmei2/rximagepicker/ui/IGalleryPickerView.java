package com.qingmei2.rximagepicker.ui;

import android.net.Uri;

import io.reactivex.Observable;

/**
 * Created by QingMei on 2018/1/19.
 */
public interface IGalleryPickerView {

    Observable<Uri> pickImage();

}
