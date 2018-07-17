package com.qingmei2.rximagepicker.ui.gallery

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.BaseSystemPickerView
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SystemGalleryPickerView : BaseSystemPickerView(), IGalleryCustomPickerView {

    override fun display(fragmentActivity: FragmentActivity,
                         @IdRes viewContainer: Int,
                         tag: String,
                         configuration: ICustomPickerConfiguration?) {
        val fragmentManager = fragmentActivity.supportFragmentManager
        val fragment: Fragment? = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            val transaction = fragmentManager.beginTransaction()
            if (viewContainer != 0) {
                transaction.add(viewContainer, this, tag).commit()
            } else {
                transaction.add(this, tag).commit()
            }
        }
    }

    override fun pickImage(): Observable<Result> {
        publishSubject = PublishSubject.create<Result>()
        return uriObserver
    }

    override fun startRequest() {
        if (!checkPermission()) {
            return
        }

        val pictureChooseIntent: Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pictureChooseIntent = Intent(Intent.ACTION_PICK)
            pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        } else {
            pictureChooseIntent = Intent(Intent.ACTION_GET_CONTENT)
        }
        pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        pictureChooseIntent.type = "image/*"

        startActivityForResult(pictureChooseIntent, BaseSystemPickerView.GALLERY_REQUEST_CODE)
    }

    override fun getActivityResultUri(data: Intent?): Uri? {
        return data?.data
    }
}
