package com.qingmei2.rximagepicker.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import io.reactivex.Observable;

public interface IPickerView extends LifecycleObserver {

    IPickerView display(FragmentManager fragmentManager,
                        @IdRes int viewContainer,
                        String tag);

    Observable<Uri> pickImage();

}
