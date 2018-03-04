package com.qingmei2.rximagepicker.ui;

import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;

import io.reactivex.Observable;

public interface IPickerView extends Serializable {

    void display(FragmentActivity fragmentActivity,
                 @IdRes int viewContainer,
                 String tag);

    Observable<Uri> pickImage();

}
