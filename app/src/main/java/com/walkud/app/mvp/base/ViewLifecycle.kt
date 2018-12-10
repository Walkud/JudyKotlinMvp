package com.walkud.app.mvp.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View

/**
 * Activity 、Fragment生命周期及部分回调，自行添加
 * Created by Zhuliya on 2018/11/7
 */
abstract class ViewLifecycle {

    /**
     * MvpActivity 、Fragment OnCreate 时调用，需要时子类实现
     */
    fun onCreate(savedInstanceState: Bundle?) {
    }

    /**
     * MvpActivity 、Fragment onStart 时调用，需要时子类实现
     */
    fun onStart() {
    }

    /**
     * MvpActivity onRestart 时调用，需要时子类实现
     */
    fun onRestart() {
    }

    /**
     * MvpActivity 、Fragment onResume 时调用，需要时子类实现
     */
    fun onResume() {
    }

    /**
     * MvpActivity 、Fragment onPause 时调用，需要时子类实现
     */
    fun onPause() {
    }

    /**
     * MvpActivity、Fragment onStop 时调用，需要时子类实现
     */
    fun onStop() {
    }

    /**
     * MvpActivity 、Fragment onDestroy 时调用，需要时子类实现
     */
    fun onDestroy() {
    }

    /**
     * MvpActivity onSaveInstanceState 时调用，需要时子类实现
     */
    fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    }

    /**
     * MvpActivity 、Fragment onActivityResult 时调用，需要时子类实现
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    /**
     * Fragment onAttach 时调用，需要时子类实现
     */
    fun onAttach(activity: Activity) {
    }

    /**
     * Fragment onViewCreated 时调用，需要时子类实现
     */
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    /**
     * Fragment onDestroyView 时调用，需要时子类实现
     */
    fun onDestroyView() {
    }

    /**
     * Fragment onDetach 时调用，需要时子类实现
     */
    fun onDetach() {
    }

    /**
     * Fragment onSaveInstanceState 时调用，需要时子类实现
     */
    fun onSaveInstanceState(outState: Bundle) {
    }

    /**
     * Fragment onActivityCreated 时调用，需要时子类实现
     */
    fun onActivityCreated(savedInstanceState: Bundle?) {
    }
}