package com.qingmei2.rximagepicker.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.net.Uri;

import io.reactivex.Observable;

public interface IPickerView extends LifecycleObserver {

    Observable<Uri> pickImage();

}
