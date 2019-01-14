package com.qingmei2.sample.system

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qingmei2.rximagepicker.core.RxImagePicker
import com.qingmei2.rximagepicker.ui.gallery.DefaultGalleryMimes
import com.qingmei2.rximagepicker.ui.gallery.DefaultSystemGalleryConfig
import com.qingmei2.sample.R
import com.qingmei2.sample.imageloader.GlideApp
import kotlinx.android.synthetic.main.activity_system.*

@SuppressLint("CheckResult")
class SystemActivity : AppCompatActivity() {

    private lateinit var defaultImagePicker: SystemImagePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system)

        initRxImagePicker()
        fabPickCamera.setOnClickListener { pickCamera() }
        fabGallery.setOnClickListener { pickGallery() }
    }

    private fun initRxImagePicker() {
        defaultImagePicker = RxImagePicker.create(SystemImagePicker::class.java)
    }

    private fun pickGallery() {
        defaultImagePicker
                .openGallery(
                        this,
                        DefaultSystemGalleryConfig.instance(
                                // mimesType = DefaultGalleryMimes.videoOnly()     // only video files
                                // mimesType = DefaultGalleryMimes.imageOnly()     // only image files, default options.
                                // mimesType = DefaultGalleryMimes.audioOnly()     // only audio files
                                mimesType = DefaultGalleryMimes.customTypes("video/*;image/*") // multiType
                        )
                )
                .subscribe { result -> onPickUriSuccess(result.uri) }
    }

    private fun pickCamera() {
        defaultImagePicker.openCamera(this)
                .subscribe { result -> onPickUriSuccess(result.uri) }
    }

    private fun onPickUriSuccess(uri: Uri) {
        GlideApp.with(this)
                .load(uri)
                .into(imageView)
    }
}
