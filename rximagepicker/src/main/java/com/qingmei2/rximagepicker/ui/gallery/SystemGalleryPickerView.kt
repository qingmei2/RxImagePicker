package com.qingmei2.rximagepicker.ui.gallery

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.BaseSystemPickerView
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import com.qingmei2.rximagepicker.ui.IGalleryCustomPickerView

import io.reactivex.Observable

class SystemGalleryPickerView : BaseSystemPickerView(), IGalleryCustomPickerView {

    override fun display(fragmentActivity: FragmentActivity,
                         @IdRes containerViewId: Int,
                         tag: String,
                         configuration: ICustomPickerConfiguration) {
        val fragmentManager = fragmentActivity.getSupportFragmentManager()
        val fragment = fragmentManager.findFragmentByTag(tag) as SystemGalleryPickerView
        if (fragment == null) {
            val transaction = fragmentManager.beginTransaction()
            if (containerViewId != 0) {
                transaction.add(containerViewId, this, tag).commit()
            } else {
                transaction.add(this, tag).commit()
            }
        }
    }

    override fun pickImage(): Observable<Result> {
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
        pictureChooseIntent.setType("image/*")

        startActivityForResult(pictureChooseIntent, BaseSystemPickerView.GALLERY_REQUEST_CODE)
    }

    override fun getActivityResultUri(data: Intent): Uri {
        return data.getData()
    }
}
