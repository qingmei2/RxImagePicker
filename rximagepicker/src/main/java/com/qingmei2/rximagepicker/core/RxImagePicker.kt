package com.qingmei2.rximagepicker.core

import com.qingmei2.rximagepicker.ui.SystemImagePicker
import java.lang.reflect.Proxy

object RxImagePicker {

    fun create(): SystemImagePicker {
        return create(SystemImagePicker::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> create(classProviders: Class<T>): T {
        val proxyProviders = ProxyProviders()

        return Proxy.newProxyInstance(
                classProviders.classLoader,
                arrayOf<Class<*>>(classProviders),
                proxyProviders) as T
    }
}
