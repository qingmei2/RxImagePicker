package com.qingmei2.rximagepicker.di.scheduler;

import android.support.annotation.RestrictTo;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

/**
 * An extra layer of packaging, using {@link IRxImagePickerSchedulers} for control thread switch.
 * <p>
 * Use {@link Schedulers#io()} and {@link AndroidSchedulers#mainThread()} directly is not
 * conducive to unit testing, you should use {@link RxImagePickerTestSchedulers} for inject {@link Scheduler}
 * in the testing.
 */
@RestrictTo(LIBRARY)
public class RxImagePickerSchedulers implements IRxImagePickerSchedulers {

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }
}
