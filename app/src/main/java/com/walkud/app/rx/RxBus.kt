package com.walkud.app.rx

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 * RxJava 事件订阅与发布
 * Created by Zhuliya on 2018/11/8
 */
object RxBus {

    private val bus: FlowableProcessor<Any> = PublishProcessor.create()

    /**
     * 发送事件
     */
    fun send(obj: Any) {
        bus.onNext(obj)
    }

    /**
     * 在调用线程中订阅
     */
    fun <T> toObservable(tClass: Class<T>): Flowable<T> {
        return bus.ofType(tClass)
    }

    /**
     * 在主线程中订阅
     */
    fun <T> toObservableMain(tClass: Class<T>): Flowable<T> {
        return bus.ofType(tClass).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 是否存在订阅者
     */
    fun hasSubscribers(): Boolean {
        return bus.hasSubscribers()
    }


}