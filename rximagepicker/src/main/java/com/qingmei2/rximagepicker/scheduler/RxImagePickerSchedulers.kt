package com.qingmei2.rximagepicker.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * An extra layer of packaging, using [IRxImagePickerSchedulers] for control thread switch.
 *
 *
 * Use [Schedulers.io] and [AndroidSchedulers.mainThread] directly is not
 * conducive to unit testing, you should use [RxImagePickerTestSchedulers] for inject [Scheduler]
 * in the testing.
 */
class RxImagePickerSchedulers : IRxImagePickerSchedulers {

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }
}
