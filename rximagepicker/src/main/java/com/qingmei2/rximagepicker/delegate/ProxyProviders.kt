package com.qingmei2.rximagepicker.delegate

import android.support.v4.app.FragmentActivity

import com.qingmei2.rximagepicker.core.IImagePickerProcessor
import com.qingmei2.rximagepicker.core.ImagePickerConfigProvider
import com.qingmei2.rximagepicker.core.RxImagePicker
import com.qingmei2.rximagepicker.di.DaggerRxImagePickerComponent
import com.qingmei2.rximagepicker.di.RxImagePickerComponent
import com.qingmei2.rximagepicker.di.RxImagePickerModule
import com.qingmei2.rximagepicker.entity.CustomPickConfigurations

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.concurrent.Callable

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single

class ProxyProviders(builder: RxImagePicker.Builder,
                     providersClass: Class<*>) : InvocationHandler {

    private val rxImagePickerProcessor: IImagePickerProcessor
    private val proxyTranslator: ProxyTranslator
    private val fragmentActivity: FragmentActivity
    private val customPickConfigurations: CustomPickConfigurations

    init {
        val component = DaggerRxImagePickerComponent.builder()
                .rxImagePickerModule(RxImagePickerModule(builder))
                .build()

        rxImagePickerProcessor = component.rxImagePickerProcessor()
        fragmentActivity = component.fragmentActivity()
        proxyTranslator = component.proxyTranslator()
        customPickConfigurations = component.customPickConfigurations()
    }

    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any {

        return Observable.defer(Callable<ObservableSource<*>> {
            val configProvider = proxyTranslator.processMethod(method, args)

            proxyTranslator.instanceProjector(configProvider, fragmentActivity)
                    .display(customPickConfigurations)

            val observable = rxImagePickerProcessor.process(configProvider)

            val methodType = method.returnType

            if (methodType == Observable::class.java)
                return@Callable Observable.just(observable)

            if (methodType == Single::class.java)
                return@Callable Observable.just<Single<*>>(Single.fromObservable(observable))

            if (methodType == Maybe::class.java)
                return@Callable Observable.just<Maybe<*>>(Maybe.fromSingle(Single.fromObservable(observable)))

            if (methodType == Flowable::class.java)
                return@Callable Observable.just(observable.toFlowable(BackpressureStrategy.MISSING))

            throw RuntimeException(method.name + " needs to return one of the next reactive types: observable, single, maybe or flowable")
        }).blockingFirst()
    }
}
