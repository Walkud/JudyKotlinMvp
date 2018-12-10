package com.walkud.app.mvp.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

/**
 * Mvp Fragment 基类
 * Created by Zhuliya on 2018/11/8
 */
abstract class MvpFragment<P> : MvcFragment() {

    /**
     * 逻辑处理实例
     */
    val presenter: P by lazy {
        getP()
    }

    /**
     * 获取逻辑处理实例，子类实现
     */
    abstract fun getP(): P

    private fun getBasePresenter(): BasePresenter<*, *> {
        return presenter as BasePresenter<*, *>
    }

    //生命周期及回调处理

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBasePresenter().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        getBasePresenter().onStart()
    }

    override fun onResume() {
        super.onResume()
        getBasePresenter().onResume()
    }

    override fun onPause() {
        super.onPause()
        getBasePresenter().onPause()
    }

    override fun onStop() {
        super.onStop()
        getBasePresenter().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        getBasePresenter().onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getBasePresenter().onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getBasePresenter().onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        getBasePresenter().onAttach(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBasePresenter().onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getBasePresenter().onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        getBasePresenter().onDetach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getBasePresenter().onActivityCreated(savedInstanceState)
    }

    //声明周期处理 End

}