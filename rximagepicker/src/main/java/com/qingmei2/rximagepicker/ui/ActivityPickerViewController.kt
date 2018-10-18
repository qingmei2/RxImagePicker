package com.qingmei2.rximagepicker.ui

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity

import com.qingmei2.rximagepicker.entity.Result

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ActivityPickerViewController private constructor() : ICustomPickerView {

    private var publishSubject: PublishSubject<Result> = PublishSubject.create()

    private lateinit var activityClass: Class<out Activity>

    fun setActivityClass(clazz: Class<out Activity>) {
        activityClass = clazz
    }

    fun resetSubject() {
        publishSubject = PublishSubject.create()
    }

    override fun display(fragmentActivity: androidx.fragment.app.FragmentActivity,
                         viewContainer: Int,
                         configuration: ICustomPickerConfiguration?) {
        resetSubject()
        fragmentActivity.startActivity(Intent(fragmentActivity, activityClass))
    }

    override fun pickImage(): Observable<Result> {
        return publishSubject
    }

    fun emitResult(result: Result) {
        publishSubject.onNext(result)
    }

    fun emitResults(results: List<Result>) {
        for (result in results) {
            emitResult(result)
        }
    }

    fun emitError(e: Throwable) {
        publishSubject.onError(e)
    }

    fun endResultEmitAndReset() {
        publishSubject.onComplete()
        resetSubject()
    }

    companion object {

        @Volatile
        private var INSTANCE: ActivityPickerViewController? = null

        val instance: ActivityPickerViewController
            get() {
                if (INSTANCE == null) {
                    synchronized(ActivityPickerViewController::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = ActivityPickerViewController()
                        }
                    }
                }
                return INSTANCE!!
            }
    }
}
