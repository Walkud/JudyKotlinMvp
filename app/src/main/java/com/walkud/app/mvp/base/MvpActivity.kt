package com.walkud.app.mvp.base

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle

/**
 * Mvp Activity 基类
 * Created by Zhuliya on 2018/11/7
 */
abstract class MvpActivity<P> : MvcActivity() {

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

    override fun onRestart() {
        super.onRestart()
        getBasePresenter().onRestart()
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

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        getBasePresenter().onSaveInstanceState(outState, outPersistentState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getBasePresenter().onActivityResult(requestCode, resultCode, data)
    }

    //声明周期处理 End
}