package com.walkud.app.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.walkud.app.BuildConfig

/**
 * 日志二次封装类
 * 便于日后统一替换
 * Created by Zhuliya on 2018/12/20
 */
object MLog {

    /**
     * 初始化日志
     */
    fun init() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 隐藏线程信息 默认：显示
                .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Walkud")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    /**
     * 打印 debug 日志
     */
    fun d(msg: String, args: Array<Any>? = null) {
        Logger.d(msg, args)
    }

    /**
     * 打印 error 日志
     */
    fun e(msg: String, throwable: Throwable) {
        Logger.e(throwable, msg)
    }

    /**
     * 打印 error 日志
     */
    fun e(msg: String, args: Array<Any>? = null, throwable: Throwable) {
        Logger.e(throwable, msg, args)
    }

    /**
     * 打印 json 格式日志
     */
    fun json(json: String) {
        Logger.json(json)
    }

}