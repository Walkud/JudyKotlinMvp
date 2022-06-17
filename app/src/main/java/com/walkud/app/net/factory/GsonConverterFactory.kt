package com.walkud.app.net.factory

import com.google.gson.Gson
import com.lemon.core.converter.Converter
import com.lemon.core.request.ContentType
import com.lemon.core.request.body.RequestBody
import com.lemon.core.request.body.ResponseBody
import java.lang.reflect.Method
import java.lang.reflect.Type

/**
 * Describe: Gson 转换器工厂
 * Created by liya.zhu on 2022/3/25
 */
class GsonConverterFactory(private val gson: Gson = Gson()) : Converter.Factory {

    override fun requestBodyConverter(type: Type, method: Method): Converter<*, RequestBody> {
        return RequestBodyConverter(gson, type)
    }

    override fun responseBodyConverter(type: Type, method: Method): Converter<ResponseBody, *> {
        return ResponseBodyConverter(gson, type)
    }

    /**
     * 请求消息体 Gson 转换器
     */
    private class RequestBodyConverter(private val gson: Gson, private val type: Type) :
        Converter<Any, RequestBody> {
        override fun convert(value: Any) =
            RequestBody.create(gson.toJson(value, type), ContentType.JSON)
    }

    /**
     * 响应消息体 Gson 转换器
     */
    private class ResponseBodyConverter(private val gson: Gson, private val type: Type) :
        Converter<ResponseBody, Any> {
        override fun convert(value: ResponseBody): Any {
            val charset = value.contentType()?.getCharset() ?: Charsets.UTF_8
            return gson.fromJson(String(value.byteArray(), charset), type)
        }
    }
}