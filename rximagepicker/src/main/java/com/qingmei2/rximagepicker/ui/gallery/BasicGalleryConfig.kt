package com.qingmei2.rximagepicker.ui.gallery

import android.content.Intent
import android.os.Build
import com.qingmei2.rximagepicker.ui.ICustomPickerConfiguration

class DefaultSystemGalleryConfig private constructor(
        private val mimesType: DefaultGalleryMimes
) : ICustomPickerConfiguration {

    fun systemIntent(): Intent = mimesType.getIntent()

    override fun onDisplay() {
        // do nothing
    }

    override fun onFinished() {
        // do nothing
    }

    companion object {

        fun instance(mimesType: DefaultGalleryMimes): DefaultSystemGalleryConfig =
                DefaultSystemGalleryConfig(
                        mimesType = mimesType
                )

        fun defaultInstance(): DefaultSystemGalleryConfig =
                DefaultSystemGalleryConfig(
                        mimesType = DefaultGalleryMimes.imageOnly()
                )
    }
}

sealed class DefaultGalleryMimes {

    abstract fun getIntent(): Intent

    abstract class BaseMimesType : DefaultGalleryMimes() {

        override fun getIntent(): Intent =
                when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    true -> Intent(Intent.ACTION_PICK)
                            .addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                    false -> Intent(Intent.ACTION_GET_CONTENT)
                }.apply {
                    putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    type = getMimesType()
                }

        abstract fun getMimesType(): String
    }

    object Video : BaseMimesType() {

        override fun getMimesType(): String = "video/*"
    }

    object Image : BaseMimesType() {

        override fun getMimesType(): String = "image/*"
    }

    object Audio : BaseMimesType() {

        override fun getMimesType(): String = "audio/*"
    }

    companion object {

        fun videoOnly(): BaseMimesType = Video

        fun imageOnly(): BaseMimesType = Image

        fun audioOnly(): BaseMimesType = Audio

        // Call this function if you want to select multiple types at the same time.
        // For example: "video/*;image/*"
        fun customTypes(types: String): BaseMimesType = object : BaseMimesType() {

            override fun getMimesType(): String = types
        }
    }
}