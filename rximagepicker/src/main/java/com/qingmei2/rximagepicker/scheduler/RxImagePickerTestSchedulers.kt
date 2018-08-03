package com.qingmei2.rximagepicker.scheduler

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

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
