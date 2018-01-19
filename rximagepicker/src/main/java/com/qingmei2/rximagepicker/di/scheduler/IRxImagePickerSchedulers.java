package com.qingmei2.rximagepicker.di.scheduler;

import io.reactivex.Scheduler;

/**
 * {@link IRxImagePickerSchedulers} is used to manage the {@link Scheduler}.
 */
public interface IRxImagePickerSchedulers {

    Scheduler ui();

    Scheduler io();
}
