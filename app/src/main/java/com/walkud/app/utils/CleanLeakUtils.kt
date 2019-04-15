package com.walkud.app.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field

/**
 * Created by xuhao on 2017/12/13.
 * desc:
 */

object CleanLeakUtils {

    private var lastSrvViewField: Field? = null

    /**
     * 修复内存泄漏，通过反射将 InputMethodManager mLastSrvView 成员属性设置为 null
     */
    private fun fixLeak(context: Context) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val arr = arrayOf("mLastSrvView")
        for (param in arr) {
            try {
                if (lastSrvViewField == null) {
                    lastSrvViewField = InputMethodManager::class.java.getDeclaredField(param)
                }

                lastSrvViewField?.let {
                    it.setAccessible(true)
                    it.set(inputMethodManager, null)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        val viewArray = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        var filed: Field
        var filedObject: Any?

        for (view in viewArray) {
            try {
                filed = inputMethodManager.javaClass.getDeclaredField(view)
                if (!filed.isAccessible) {
                    filed.isAccessible = true
                }
                filedObject = filed.get(inputMethodManager)
                if (filedObject != null && filedObject is View) {
                    val fileView = filedObject as View?
                    if (fileView!!.context === context) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        filed.set(inputMethodManager, null) // 置空，破坏掉path to gc节点
                    } else {
                        break// 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }

        }
    }

    /**
     * 修复 InputMethodManager 内存泄漏问题
     */
    fun fixInputMethodManagerLeak(destContext: Context?) {
        destContext?.let {
            fixLeak(it)
        }
    }
}
