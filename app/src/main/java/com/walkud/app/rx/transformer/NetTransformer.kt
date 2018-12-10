package com.walkud.app.rx.transformer

import com.walkud.app.rx.SchedulersCompat
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 网络请求事务
 * 1、处理异步调度
 * 2、处理错误码逻辑
 * 3、处理统一请求延迟，避免
 * Created by Zhuliya on 2018/11/21
 */
class NetTransformer<T> : ObservableTransformer<T, T> {
    override fun apply(observable: Observable<T>): ObservableSource<T> {
        val hideObservable: Observable<T> = observable.hide()

        val resultObservable: Observable<T> = observable.flatMap {
            //在此统一处理错误码（比如重连，密匙更新等）

            Observable.just(it)
        }.observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    it.printStackTrace()
                }

        //如果网络瞬间完成影响体验，则可以在此统一延迟请求
        return Observable.timer(100, TimeUnit.MILLISECONDS).flatMap {
            resultObservable.compose(SchedulersCompat.applyIoSchedulers())
        }
    }
}