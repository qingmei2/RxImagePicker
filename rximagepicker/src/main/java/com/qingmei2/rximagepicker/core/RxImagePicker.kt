package com.qingmei2.rximagepicker.core

import com.qingmei2.rximagepicker.delegate.ProxyProviders
import com.qingmei2.rximagepicker.ui.SystemImagePicker
import java.lang.reflect.Proxy

object RxImagePicker {

    fun create(): SystemImagePicker {
        return create(SystemImagePicker::class.java)
    }

    fun <T> create(classProviders: Class<T>): T {
        val proxyProviders = ProxyProviders(classProviders)

        return Proxy.newProxyInstance(
                classProviders.classLoader,
                arrayOf<Class<*>>(classProviders),
                proxyProviders) as T
    }
}
