package com.qingmei2.rximagepicker.ui.camera

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.BaseSystemPickerView
import com.qingmei2.rximagepicker.ui.ICameraCustomPickerView
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import io.reactivex.Observable

class SystemCameraPickerView : BaseSystemPickerView(), ICameraCustomPickerView {

    private var cameraPictureUrl: Uri? = null

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
        return uriObserver
    }

    override fun startRequest() {
        if (!checkPermission()) {
            return
        }
        cameraPictureUrl = createImageUri()
        val pictureChooseIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl)

        startActivityForResult(pictureChooseIntent, BaseSystemPickerView.CAMERA_REQUEST_CODE)
    }

    override fun getActivityResultUri(data: Intent?): Uri? {
        return cameraPictureUrl
    }

    private fun createImageUri(): Uri? {
        val contentResolver = activity!!.contentResolver
        val cv = ContentValues()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        cv.put(MediaStore.Images.Media.TITLE, timeStamp)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    }
}
