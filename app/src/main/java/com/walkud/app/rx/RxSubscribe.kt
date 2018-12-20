package com.walkud.app.rx

import android.os.Looper
import com.walkud.app.common.exception.SubscribeException
import com.walkud.app.utils.MLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 二次封装Subscriber
 * 统一处理异常
 * Created by Zhuliya on 2018/11/8
 */
abstract class RxSubscribe<T> : Observer<T> {

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        try {
            call(t)
        } catch (e: Exception) {
            onError(SubscribeException(e))
        }
    }

    override fun onError(e: Throwable) {
        MLog.e("isUiThread:${Looper.getMainLooper() == Looper.myLooper()}", e)
    }

    abstract fun call(result: T)

}