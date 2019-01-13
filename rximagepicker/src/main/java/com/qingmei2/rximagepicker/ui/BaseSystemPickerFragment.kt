package com.qingmei2.rximagepicker.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.function.parseResultNoExtraData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseSystemPickerFragment : Fragment() {

    private val attachedSubject = PublishSubject.create<Boolean>()

    protected var publishSubject: PublishSubject<Result> = PublishSubject.create<Result>()

    private val canceledSubject: PublishSubject<Int> = PublishSubject.create<Int>()

    val uriObserver: Observable<Result>
        get() {
            requestPickImage()
            return publishSubject.takeUntil(canceledSubject)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachedSubject.onNext(true)
        attachedSubject.onComplete()
    }

    private fun requestPickImage() {
        if (!isAdded) {
            attachedSubject.subscribe { startRequest() }
        } else {
            startRequest()
        }
    }

    abstract fun startRequest()

    abstract fun getActivityResultUri(data: Intent?): Uri?

    private fun onImagePicked(uri: Uri?) {
        if (uri != null) {
            publishSubject.onNext(parseResultNoExtraData(uri))
        }
        publishSubject.onComplete()
        closure()
    }

    /**
     * When the pick image behavior ending, remove this fragment from the activity.
     */
    private fun closure() {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.remove(this)
        fragmentTransaction.commit()
    }

    protected fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRequest()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE, CAMERA_REQUEST_CODE ->
                    onImagePicked(
                            getActivityResultUri(data)
                    )
            }
        } else {
            canceledSubject.onNext(requestCode)
        }
    }

    companion object {

        const val GALLERY_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 101
    }
}
