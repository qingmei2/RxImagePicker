package com.qingmei2.rximagepicker.mock

import com.qingmei2.rximagepicker.config.sources.Camera
import com.qingmei2.rximagepicker.config.sources.Gallery

/**
 * Created by QingMei on 2018/1/22.
 */
interface TestImagePicker {

    @Camera
    fun camera()

    @Gallery
    fun gallery()
}
