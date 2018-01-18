package com.qingmei2.rximagepicker.di.scheduler;

import io.reactivex.Scheduler;

/**
 * {@link IRxImagePickerSchedulers} is used to manage the {@link Scheduler}.
 * <p>
 * Created by QingMei on 2018/1/18.
 */
public interface IRxImagePickerSchedulers {

    Scheduler ui();

    Scheduler io();
}
