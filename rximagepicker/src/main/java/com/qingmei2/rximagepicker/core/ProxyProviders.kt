package com.qingmei2.rximagepicker.core

import com.qingmei2.rximagepicker.scheduler.RxImagePickerSchedulers
import io.reactivex.*
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.concurrent.Callable

class ProxyProviders : InvocationHandler {

    private val rxImagePickerProcessor = ConfigProcessor(
            RxImagePickerSchedulers()
    )
    private val proxyTranslator = ProxyTranslator()

    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {

        return Observable.defer(Callable<ObservableSource<*>> {
            val configProvider = proxyTranslator.processMethod(method, args)

            ImagePickerController(configProvider).display()

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
