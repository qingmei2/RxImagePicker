package com.qingmei2.rximagepicker.di.scheduler

import io.reactivex.Scheduler

/**
 * [IRxImagePickerSchedulers] is used to manage the [Scheduler].
 */
interface IRxImagePickerSchedulers {

    fun ui(): Scheduler

    fun io(): Scheduler
}
