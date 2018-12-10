package com.walkud.app.rx.transformer

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * 什么也没处理的事务
 * Created by Zhuliya on 2018/11/23
 */
class EmptyTransformer<T> : ObservableTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
    }
}