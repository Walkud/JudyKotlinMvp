package com.walkud.app.utils

import android.annotation.SuppressLint
import android.content.Context

/**
 * Created by Zhuliya on 2018/12/6
 */
class ContextUtil private constructor() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        fun setContext(cxt: Context) {
            context = cxt.applicationContext
        }

        fun getContext(): Context {
            return context!!
        }
    }

}