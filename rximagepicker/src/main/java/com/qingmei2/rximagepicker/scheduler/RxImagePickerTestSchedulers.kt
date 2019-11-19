package com.qingmei2.rximagepicker.scheduler

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * An extra layer of packaging, using [RxImagePickerTestSchedulers] for easier testing.
 */
class RxImagePickerTestSchedulers : IRxImagePickerSchedulers {

    override fun ui(): Scheduler {
        return Schedulers.io()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }
}
