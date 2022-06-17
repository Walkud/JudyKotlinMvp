package com.walkud.app.view

import android.app.Activity
import android.app.ProgressDialog
import com.classic.common.MultipleStatusView
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.common.extensions.isNetworkError

/**
 * 进度 View 接口，用于显示或隐藏进度显示
 */
abstract class ProgressView {

    /**
     * 显示进度
     */
    open fun show() {}

    /**
     * 正常回调进度
     */
    open fun call() {}

    /**
     * 错误回调进度
     */
    open fun error(e: Exception) {}

    /**
     * 隐藏进度
     */
    open fun dismiss() {}

    companion object {
        val EMPTY = EmptyProgress()
    }

    /**
     * 下拉刷新样式
     */
    class SmartRefreshProgress(private val smartRefresh: SmartRefreshLayout) : ProgressView() {
        override fun dismiss() {
            smartRefresh.finishRefresh(0)
            smartRefresh.finishLoadmore(0)
        }
    }

    /**
     * 多状态进度样式
     */
    class MultipleStatusProgress(private val multipleStatusView: MultipleStatusView) :
        ProgressView() {
        override fun show() {
            multipleStatusView.showLoading()
        }

        override fun call() {
            multipleStatusView.showContent()
        }

        override fun error(e: Exception) {
            if (e.isNetworkError()) {
                multipleStatusView.showNoNetwork()
            } else {
                multipleStatusView.showError()
            }
        }
    }

    /**
     * 请稍等 ProgressDialog 进度样式
     */
    class WaitDialgProgress(activity: Activity, msg: String = "请稍等...") : ProgressView() {
        private val progressDialog by lazy {
            ProgressDialog(activity).apply {
                setMessage(msg)
            }
        }

        override fun show() {
            progressDialog.show()
        }

        override fun dismiss() {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }
    }

    /**
     * 空进度样式
     */
    class EmptyProgress : ProgressView()
}