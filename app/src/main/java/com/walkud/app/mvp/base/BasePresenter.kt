package com.walkud.app.mvp.base

import com.walkud.app.utils.ReflectionUtils
import kotlin.properties.Delegates


/**
 * Mvp Presenter基类
 * Created by Zhuliya on 2018/11/2
 */
open class BasePresenter<V : Any, out M> : ViewLifecycle() {

    /**
     * UI视图，即Activity
     */
    var view: V by Delegates.notNull()

    /**
     * 业务模型，即XXXModel
     */
    val model: M by lazy {
        ReflectionUtils.getSuperClassGenricType<M>(this, 1)
    }
}