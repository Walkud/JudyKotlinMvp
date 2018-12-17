package com.walkud.app.mvp.base

import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
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
     * 业务模型，即XXXModel，这里使用java反射(kotlin反射太慢，暂时不建议使用)创建示例，省去在每个Presenter中创建实例
     */
    val model: M by lazy {
        ReflectionUtils.getSuperClassGenricType<M>(this, 1)
    }

    /**
     * 绑定 RxJava OnDestroy 生命周期，封装常用的即可
     * 优点：当Presenter对应的UI的类型(比如Activity或Fragment)切换时，不会崩溃
     * 缺点：需要对所有需要的声明周期，都需要进行封装
     */
    fun <VT> bindUntilOnDestroyEvent(): LifecycleTransformer<VT> {
        if (view is RxFragment) {
            return bindFragmentUntilEvent(FragmentEvent.DESTROY)
        }
        return bindActivityUntilEvent(ActivityEvent.DESTROY)
    }

    /**
     * 绑定 RxActivity 指定生命周期
     * 有点：灵活
     * 缺点：当Presenter对应的UI的类型(比如Activity或Fragment)切换时，需要修改对应的Presenter
     */
    fun <VT> bindActivityUntilEvent(event: ActivityEvent): LifecycleTransformer<VT> {
        return (view as RxAppCompatActivity).bindUntilEvent(event)
    }

    /**
     * 绑定 RxFragment 指定的声明周期
     */
    fun <VT> bindFragmentUntilEvent(event: FragmentEvent): LifecycleTransformer<VT> {
        return (view as RxFragment).bindUntilEvent(event)
    }
}