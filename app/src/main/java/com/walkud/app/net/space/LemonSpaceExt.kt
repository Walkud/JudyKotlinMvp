package com.walkud.app.net.space

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.walkud.app.view.ProgressView

/**
 * LemonSpace 统一封装扩展文件
 */

/**
 * 构建进度 UI LemonSpace
 */
fun <T> LemonSpace<T>.bindUi(
    progressView: ProgressView,
    owner: LifecycleOwner,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): LemonSpace<T> {
    return bindLifecycle(owner, event)//绑定 UI 生命周期
        .doStart { progressView.show() } //开始时显示进度 UI
        .doCall { progressView.call() }//正常回调时显示内容 UI
        .doError { progressView.error(it) }//错误时显示错误内容 UI
        .doEnd { progressView.dismiss() } //结束时隐藏进度 UI
}