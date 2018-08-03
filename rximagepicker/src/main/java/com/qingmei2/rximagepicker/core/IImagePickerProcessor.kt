package com.qingmei2.rximagepicker.core

import com.qingmei2.rximagepicker.providers.ConfigProvider
import com.qingmei2.rximagepicker.providers.RuntimeProvider
import io.reactivex.Observable

interface IImagePickerProcessor {

    fun process(configProvider: ConfigProvider,
                runtimeProvider: RuntimeProvider): Observable<*>
}
