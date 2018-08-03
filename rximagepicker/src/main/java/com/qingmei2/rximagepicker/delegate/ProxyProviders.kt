package com.qingmei2.rximagepicker.delegate

import com.qingmei2.rximagepicker.core.IImagePickerProcessor
import com.qingmei2.rximagepicker.core.ImagePickerConfigProcessor
import com.qingmei2.rximagepicker.core.ImagePickerProjector
import com.qingmei2.rximagepicker.scheduler.RxImagePickerSchedulers

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.concurrent.Callable

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single

class ProxyProviders(providersClass: Class<*>) : InvocationHandler {

    private val rxImagePickerProcessor: IImagePickerProcessor
    private val proxyTranslator: ProxyTranslator

    init {
        rxImagePickerProcessor = ImagePickerConfigProcessor(
                RxImagePickerSchedulers()
        )
        proxyTranslator = ProxyTranslator()
    }

    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {

        return Observable.defer(Callable<ObservableSource<*>> {
            val configProvider = proxyTranslator.processMethodForStaticConfig(method)
            val runtimeProvider = proxyTranslator.processMethodForRuntimeParams(method, args)

            ImagePickerProjector(configProvider, runtimeProvider).display()

            val observable = rxImagePickerProcessor.process(configProvider, runtimeProvider)

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
