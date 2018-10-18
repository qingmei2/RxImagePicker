package com.qingmei2.sample.zhihu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.Glide
import com.qingmei2.rximagepicker.core.RxImagePicker
import com.qingmei2.rximagepicker.entity.Result
import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension_zhihu.ZhihuConfigurationBuilder
import com.qingmei2.rximagepicker_extension_zhihu.ui.ZhihuImagePickerActivity
import com.qingmei2.sample.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_zhihu.*

class ZhihuActivity : AppCompatActivity() {

    private lateinit var rxImagePicker: ZhihuImagePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu)

        initRxImagePicker()
        fabPickCamera.setOnClickListener { checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA) }
        fabPickGalleryNormal.setOnClickListener { checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL) }
        fabPickGalleryDracula.setOnClickListener { checkPermissionAndRequest(REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_Dracula) }
    }

    private fun checkPermissionAndRequest(requestCode: Int) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode)
        } else {
            onPermissionGrant(requestCode)
        }
    }

    private fun initRxImagePicker() {
        rxImagePicker = RxImagePicker
                .create(ZhihuImagePicker::class.java)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionGrant(requestCode)
        } else {
            Toast.makeText(this, "Please allow the Permission first.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onPermissionGrant(requestCode: Int) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA -> openCamera()
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL -> openGalleryAsNormal()
            else -> openGalleryAsDracula()
        }
    }

    /**
     * open Camera.
     */
    private fun openCamera() {
        rxImagePicker.openCamera(this)
                .subscribe(fetchUriObserver())
    }

    /**
     * Open Gallery as Zhihu normal theme.
     */
    private fun openGalleryAsNormal() {
        rxImagePicker.openGalleryAsNormal(this,
                ZhihuConfigurationBuilder(MimeType.ofImage(), false)
                        .maxSelectable(9)
                        .countable(true)
                        .spanCount(4)
                        .theme(R.style.Zhihu_Normal)
                        .build())
                .subscribe(fetchUriObserver())
    }

    /**
     * Open Gallery as Zhihu dracula theme.
     */
    private fun openGalleryAsDracula() {
        rxImagePicker.openGalleryAsDracula(this,
                ZhihuConfigurationBuilder(MimeType.ofImage(), false)
                        .spanCount(3)
                        .maxSelectable(1)
                        .theme(R.style.Zhihu_Dracula)
                        .build())
                .subscribe(fetchUriObserver())
    }

    private fun fetchUriObserver(): Observer<Result> = object : Observer<Result> {

        override fun onSubscribe(d: Disposable) {

        }

        override fun onNext(result: Result) {
            Glide.with(this@ZhihuActivity)
                    .load(result.uri)
                    .into(imageView)
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
            Toast.makeText(this@ZhihuActivity, "Failed: " + e.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun onComplete() {

        }
    }

    companion object {

        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_CAMERA = 99
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_NORMAL = 100
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_GALLERY_Dracula = 101
    }
}
