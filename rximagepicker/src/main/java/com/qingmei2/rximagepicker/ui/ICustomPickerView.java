package com.qingmei2.rximagepicker.ui;

import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;

import io.reactivex.Observable;

public interface ICustomPickerView {

    void display(FragmentActivity fragmentActivity,
                 @IdRes int viewContainer,
                 String tag,
                 ICustomPickerConfiguration configuration);

    Observable<Uri> pickImage();

}
