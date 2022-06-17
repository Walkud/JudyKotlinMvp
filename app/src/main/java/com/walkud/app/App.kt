package com.walkud.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.walkud.app.utils.ContextUtil
import com.walkud.app.utils.DisplayManager
import com.walkud.app.utils.MLog

/**
 * Application
 * Created by Zhuliya on 2018/11/7
 */
class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this) //分包处理，解决64k问题
    }

    override fun onCreate() {
        super.onCreate()
        ContextUtil.setContext(this)

        MLog.init()//初始化日志
        DisplayManager.init(this)//初始化UI显示工具类
    }


}