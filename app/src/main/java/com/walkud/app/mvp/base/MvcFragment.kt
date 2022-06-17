package com.walkud.app.mvp.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.view.ProgressView
import java.lang.Exception

/**
 * Mvc Fragment 基类
 * Created by Zhuliya on 2018/11/8
 */
abstract class MvcFragment : Fragment() {

    protected var mContentView: View? = null
    private var mIsLoadedData = false

    /**
     * 创建View
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            mContentView = LayoutInflater.from(activity).inflate(getLayoutId(), null)
        } else {
            mContentView!!.parent?.let {
                (it as ViewGroup).removeView(mContentView)
            }
        }
        return mContentView!!
    }

    /**
     * 创建View完成，调用子类初始化，避免Kotlin在onCreateView中直接使用控件出现空指针
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState, mContentView!!)
        addListener()
        processLogic(savedInstanceState)
    }

    /**
     * 设置用户可见状态
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isResumed) {
            handleOnVisibilityChangedToUser(isVisibleToUser)
        }
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            handleOnVisibilityChangedToUser(true)
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint) {
            handleOnVisibilityChangedToUser(false)
        }
    }

    /**
     * 处理对用户是否可见
     *
     * @param isVisibleToUser
     */
    private fun handleOnVisibilityChangedToUser(isVisibleToUser: Boolean) {

        if (isVisibleToUser) {
            // 对用户可见
            if (!mIsLoadedData) {
                Logger.d(javaClass.simpleName + " 懒加载一次")
                mIsLoadedData = true
                onLazyLoadOnce()
            }
            Logger.d(javaClass.simpleName + " 对用户可见")
            onVisibleToUser()
        } else {
            // 对用户不可见
            Logger.d(javaClass.simpleName + " 对用户不可见")
            onInvisibleToUser()
        }
    }

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    open fun onLazyLoadOnce() {}

    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    open fun onVisibleToUser() {}

    /**
     * 对用户不可见时触发该方法
     */
    open fun onInvisibleToUser() {}

    /**
     * 获取RootLayout布局Id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化 View 控件
     */
    abstract fun initView(savedInstanceState: Bundle?, rootView: View)

    /**
     * 给 View 控件添加事件监听器
     */
    open fun addListener() {}

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    open fun processLogic(savedInstanceState: Bundle?) {}

    /**
     * 跳转
     * @param cls 类
     */
    fun forward(cls: Class<*>) {
        activity?.apply {
            forward(Intent(activity, cls))
        }
    }

    /**
     * 跳转
     * @param intent
     */
    fun forward(intent: Intent) {
        startActivity(intent)
    }

    /**
     * 跳转并关闭当前页面
     * @param intent
     */
    fun forwardAndFinish(cls: Class<*>) {
        forward(cls)
        backward()
    }

    /**
     * 跳转并关闭当前页面
     */
    fun forwardAndFinish(intent: Intent) {
        forward(intent)
        backward()
    }

    /**
     * 回退
     */
    fun backward() {
        activity?.finish()
    }

    /**
     * 显示Toast
     * @param resId 文本资源Id
     */
    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    /**
     * 显示Toast
     * @param msg 文本
     */
    fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 显示异常信息Toast
     */
    fun showExceptionToast(exception: Exception) {
        showToast(ExceptionHandle.handleExceptionMsg(exception))
    }

    /**
     * 跳转
     * @param cls 类
     */
    fun forword(cls: Class<*>) {
        forword(Intent(activity, cls))
    }

    /**
     * 跳转
     * @param intent
     */
    fun forword(intent: Intent) {
        startActivity(intent)
    }

    /**
     * 获取异步进度加载事务，子类复写
     * 默认返回 空事务或者默认进度框事务
     */
    open fun getWaitProgressView(): ProgressView {
        if (activity == null) {
            //防止空指针
            return ProgressView.EMPTY
        }
        return ProgressView.WaitDialgProgress(activity!!, "")
    }

    /**
     * 获取异步进度下拉或上拉加载事务，子类复写
     * 默认返回 空事务
     */
    open fun getSmartRefreshProgressView(): ProgressView = ProgressView.EMPTY

    /**
     * 获取进度、错误、内容切换View事务，子类复写
     */
    open fun getMultipleStatusProgressView(): ProgressView = ProgressView.EMPTY


}