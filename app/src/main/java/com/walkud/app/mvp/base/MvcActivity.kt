package com.walkud.app.mvp.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.walkud.app.R
import com.walkud.app.common.exception.ExceptionHandle
import com.walkud.app.common.extensions.closeKeyBoard
import com.walkud.app.view.ProgressView
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception

/**
 * Activity 基类
 * Created by Zhuliya on 2018/11/7
 */
abstract class MvcActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    var permissionFlag = false//权限设置跳转记录标记

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initView(savedInstanceState)
        addListener()
        processLogic(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (permissionFlag) {//是否需要重新检查权限标记
            permissionFlag = false
            recheckPermissions()
        }
    }

    /**
     * 获取RootLayout布局Id
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化 View 控件
     */
    abstract fun initView(savedInstanceState: Bundle?)

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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
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
    fun forward(cls: Class<*>) {
        closeKeyBoard()
        forward(Intent(this, cls))
    }

    /**
     * 跳转
     * @param intent
     */
    fun forward(intent: Intent) {
        closeKeyBoard()
        startActivity(intent)
    }

    /**
     * 跳转并关闭当前页面
     * @param intent
     */
    fun forwardAndFinish(cls: Class<*>) {
        forward(cls)
        finish()
    }

    /**
     * 跳转并关闭当前页面
     */
    fun forwardAndFinish(intent: Intent) {
        forward(intent)
        finish()
    }

    /**
     * 回退
     */
    fun backward() {
        closeKeyBoard()
        finish()
    }

    fun requestPermissions(requestCode: Int, rationale: String, vararg permissions: String) {
        EasyPermissions.requestPermissions(
            this, rationale,
            requestCode, *permissions
        )
    }

    /**
     * EasyPermissions 请求权限回调处理
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 显示权限申请说明
     * @param permission 说明文本
     */
    fun showPermissionDialog(permission: String) {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(permission)
            .setTitle(getString(R.string.string_help_text, permission))
            .setPositiveButton("设置") { _, _ ->
                permissionFlag = true
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                forward(intent)
            }
            .create()
            .show()
    }

    /**
     * 重新检查权限，子类复写
     * 当跳转至设置页面后，回到当前页面时会调用此方法，子类可重新检查权限是否设置
     */
    open fun recheckPermissions() {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    /**
     * 获取异步进度加载事务，子类复写
     * 默认返回 默认进度框事务
     */
    open fun getPrgressProgressView() = ProgressView.WaitDialgProgress(this, "")

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