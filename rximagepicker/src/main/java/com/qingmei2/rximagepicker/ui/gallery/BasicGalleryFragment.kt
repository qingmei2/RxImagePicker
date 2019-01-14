package com.qingmei2.rximagepicker.ui.gallery

import android.content.Intent
import android.net.Uri
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.BaseSystemPickerFragment
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BasicGalleryFragment : BaseSystemPickerFragment(), IGalleryCustomPickerView {

    private var mDefaultSystemGalleryConfig: DefaultSystemGalleryConfig? = null

    override fun display(fragmentActivity: FragmentActivity,
                         @IdRes viewContainer: Int,
                         configuration: ICustomPickerConfiguration?) {
        injectConfigurations(configuration)

        val fragmentManager = fragmentActivity.supportFragmentManager
        val fragment: Fragment? = fragmentManager.findFragmentByTag(tag)
        val transaction = fragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.remove(fragment)
        }
        if (viewContainer != 0) {
            transaction.add(viewContainer, this, tag)
        } else {
            transaction.add(this, tag)
        }
        transaction.commitAllowingStateLoss()
    }

    private fun injectConfigurations(configuration: ICustomPickerConfiguration?) {
        when (configuration) {
            is DefaultSystemGalleryConfig ->
                this.mDefaultSystemGalleryConfig = configuration
            else -> {
                // do nothing
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

        val mTemp = mDefaultSystemGalleryConfig ?: DefaultSystemGalleryConfig.defaultInstance()
        mTemp.apply {
            startActivityForResult(this.systemIntent(), BaseSystemPickerFragment.GALLERY_REQUEST_CODE)
        }
    }

    override fun getActivityResultUri(data: Intent?): Uri? {
        return data?.data
    }
}
