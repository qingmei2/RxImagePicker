package com.qingmei2.rximagepicker.mock

import com.qingmei2.rximagepicker.config.sources.Camera
import com.qingmei2.rximagepicker.config.sources.Gallery

interface TestImagePicker {

    @Camera
    fun camera()

    @Gallery
    fun gallery()
}
