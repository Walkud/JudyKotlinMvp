package com.walkud.app.rx.transformer

import com.classic.common.MultipleStatusView
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.walkud.app.common.exception.ExceptionHandle
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * 进度、错误、内容切换View事务，免去在接口回调中添加控制逻辑
 * Created by Zhuliya on 2018/11/21
 */
class MultipleStatusViewTransformer<T>(private var multipleStatusView: MultipleStatusView?) : ObservableTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    multipleStatusView?.showLoading()
                }
                .doOnError {
                    onError(it)
                }.doOnNext {
                    doOnNext()
                }

    }

    /**
     * 加载错误,显示异常布局
     */
    private fun onError(e: Throwable) {
        ExceptionHandle.handleException(e)
        if (ExceptionHandle.errorCode == ErrorStatus.NETWORK_ERROR) {
            multipleStatusView?.showNoNetwork()
        } else {
            multipleStatusView?.showError()
        }
        multipleStatusView = null
    }

    /**
     * 加载成功，显示内容布局
     */
    private fun doOnNext() {
        multipleStatusView?.showContent()
        multipleStatusView = null
    }

}