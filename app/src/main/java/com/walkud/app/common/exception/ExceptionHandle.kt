package com.walkud.app.common.exception

import com.google.gson.JsonParseException
import com.hazz.kotlinmvp.net.exception.ApiException
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.orhanobut.logger.Logger
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.ParseException

/**
 * Created by xuhao on 2017/12/5.
 * desc: 异常处理类
 * Modify by Zhuliya on 2018/12/20
 */
class ExceptionHandle {

    companion object {
        /**
         * 判断并返回异常对应的文本信息
         */
        fun handleExceptionMsg(e: Throwable): String {
            val errorMsg = when (e) {
                is SocketTimeoutException,
                is ConnectException -> "网络连接异常"
                is JsonParseException,
                is JSONException,
                is ParseException -> "数据解析异常"
                is ApiException -> e.message.toString()
                is IllegalArgumentException -> "参数错误"
                is SubscribeException -> "跑偏了，稍后请重试!"
                else -> "未知错误，可能抛锚了吧~"
            }

            Logger.e(errorMsg, errorMsg, e)

            return errorMsg
        }

        /**
         * 判断并返回异常对应的Code
         */
        private fun handleExceptionCode(e: Throwable) = when (e) {
            is SocketTimeoutException,
            is ConnectException -> ErrorStatus.NETWORK_ERROR //网络错误
            is JsonParseException,
            is JSONException,
            is ApiException,
            is IllegalArgumentException,
            is ParseException -> ErrorStatus.SERVER_ERROR //服务器内部错误
            is SubscribeException -> ErrorStatus.LCOAL_LOGIC_ERROR //订阅逻辑本地错误
            else -> ErrorStatus.UNKNOWN_ERROR //未知错误
        }

        /**
         * 是否为指定的异常类型
         */
        fun isExceptionCode(e: Throwable, code: Int): Boolean {
            return handleExceptionCode(e) == code
        }

    }


}
