package com.qingmei2.rximagepicker.core

import io.reactivex.Observable

interface IImagePickerProcessor {

    fun process(configProvider: ImagePickerConfigProvider): Observable<*>
}
