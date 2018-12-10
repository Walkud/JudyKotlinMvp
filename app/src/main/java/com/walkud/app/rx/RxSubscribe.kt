package com.walkud.app.rx

import android.os.Looper
import com.orhanobut.logger.Logger
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 二次封装Subscriber
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
            onError(e)
        }
    }

    override fun onError(e: Throwable) {
        Logger.e("isUiThread:${Looper.getMainLooper() == Looper.myLooper()}", e)
    }

    abstract fun call(result: T)

}