package com.qingmei2.rximagepicker.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.function.*

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

import android.app.Activity.RESULT_OK

abstract class BaseSystemPickerView : Fragment() {

    private val attachedSubject = PublishSubject.create<Boolean>()

    private var publishSubject: PublishSubject<Result>? = null

    private var canceledSubject: PublishSubject<Int>? = null

    val uriObserver: Observable<Result>
        get() {
            publishSubject = PublishSubject.create()
            canceledSubject = PublishSubject.create()

            requestPickImage()
            return publishSubject!!.takeUntil(canceledSubject!!)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachedSubject.onNext(true)
        attachedSubject.onComplete()
    }

    private fun requestPickImage() {
        if (!isAdded()) {
            attachedSubject.subscribe( { startRequest() })
        } else {
            startRequest()
        }
    }

    abstract fun startRequest()

    abstract fun getActivityResultUri(data: Intent): Uri

    private fun onImagePicked(uri: Uri) {
        if (publishSubject != null) {
            publishSubject!!.onNext(parseResultNoExtraData(uri))
            publishSubject!!.onComplete()
        }
        closure()
    }

    /**
     * When the pick image behavior ending, remove this fragment from the activity.
     */
    private fun closure() {
        val fragmentManager = getFragmentManager()
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.remove(this)
        fragmentTransaction.commit()
    }

    protected fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(getActivity()!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRequest()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE, CAMERA_REQUEST_CODE -> onImagePicked(getActivityResultUri(data))
            }
        } else {
            canceledSubject!!.onNext(requestCode)
        }
    }

    companion object {

        val GALLERY_REQUEST_CODE = 100
        val CAMERA_REQUEST_CODE = 101
    }
}
