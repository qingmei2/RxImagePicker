package com.qingmei2.rximagepicker.di.scheduler;

import android.support.annotation.RestrictTo;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static android.support.annotation.RestrictTo.Scope.TESTS;

/**
 * An extra layer of packaging, using {@link RxImagePickerTestSchedulers} for easier testing.
 */
@RestrictTo(TESTS)
public class RxImagePickerTestSchedulers implements IRxImagePickerSchedulers {

    @Override
    public Scheduler ui() {
        return Schedulers.io();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }
}
