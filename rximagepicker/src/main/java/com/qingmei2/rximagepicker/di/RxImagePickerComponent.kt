package com.qingmei2.rximagepicker.di

import android.support.v4.app.FragmentActivity

import com.qingmei2.rximagepicker.core.IImagePickerProcessor
import com.qingmei2.rximagepicker.delegate.ProxyTranslator
import com.qingmei2.rximagepicker.entity.CustomPickConfigurations

import dagger.Component

@Component(modules = arrayOf(RxImagePickerModule::class))
interface RxImagePickerComponent {

    fun rxImagePickerProcessor(): IImagePickerProcessor

    fun proxyTranslator(): ProxyTranslator

    fun fragmentActivity(): FragmentActivity

    fun customPickConfigurations(): CustomPickConfigurations
}
