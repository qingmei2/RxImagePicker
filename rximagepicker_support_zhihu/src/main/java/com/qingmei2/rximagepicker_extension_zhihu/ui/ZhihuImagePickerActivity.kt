package com.qingmei2.rximagepicker_extension_zhihu.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker.ui.ActivityPickerViewController
import com.qingmei2.rximagepicker.ui.camera.BasicCameraFragment
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.ui.adapter.AlbumMediaAdapter
import com.qingmei2.rximagepicker_extension_zhihu.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ZhihuImagePickerActivity : AppCompatActivity(), AlbumMediaAdapter.OnPhotoCapture {

    private val fragment: ZhihuImagePickerFragment = ZhihuImagePickerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(SelectionSpec.instance.themeId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_zhihu)

        requestPermissionAndDisplayGallery()
    }

    private fun requestPermissionAndDisplayGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 99)
        } else {
            displayPickerView()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            displayPickerView()
        } else {
            closure()
        }
    }

    @SuppressLint("CheckResult")
    private fun displayPickerView() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commitAllowingStateLoss()

        fragment.pickImage().subscribe(observer)
    }

    override fun capture() {
        BasicCameraFragment()
                .apply {
                    display(
                            fragmentActivity = this@ZhihuImagePickerActivity,
                            viewContainer = R.id.fl_container,
                            configuration = null
                    )
                    pickImage().subscribe(observer)
                }
    }

    private val observer = object : Observer<Result> {

        override fun onComplete() = closure()

        override fun onSubscribe(p0: Disposable) {
            // do nothing
        }

        override fun onNext(result: Result) =
                ActivityPickerViewController.instance.emitResult(result = result)

        override fun onError(e: Throwable) =
                ActivityPickerViewController.instance.emitError(e)

    }

    fun closure() {
        ActivityPickerViewController.instance.endResultEmitAndReset()
        finish()
    }

    override fun onBackPressed() {
        closure()
    }

    companion object {

        const val REQUEST_CODE_PREVIEW = 23
    }

}
