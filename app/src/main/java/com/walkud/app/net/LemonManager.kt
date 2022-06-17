package com.walkud.app.net

import com.walkud.app.api.UrlConstant
import com.lemon.core.Lemon
import com.lemon.core.client.LemonClient
import com.lemon.core.interceptor.Interceptor
import com.lemon.core.request.Response
import com.lemon.log.LemonLogInterceptor
import com.lemon.log.LemonLogLevel
import com.walkud.app.BuildConfig
import com.walkud.app.api.ApiService
import com.walkud.app.net.factory.GsonConverterFactory
import com.walkud.app.net.space.LemonSpaceApiAdapterFactory
import com.walkud.app.utils.AppUtils
import com.walkud.app.utils.ContextUtil
import com.walkud.app.utils.NetworkUtil
import com.walkud.app.utils.Preference

/**
 * Created by xuhao on 2017/11/16.
 *
 */

object LemonManager {

    private val lemon by lazy {
        Lemon.build {
            setApiUrl(UrlConstant.BASE_URL)
            addConverterFactory(GsonConverterFactory())
            addApiAdapterFactory(LemonSpaceApiAdapterFactory())
//              .addInterceptor(addCacheInterceptor())//Lemon未实现缓存功能
            addInterceptor(addQueryParameterInterceptor())
            addInterceptor(addHeaderInterceptor())
            setHttpClient(
                LemonClient.Builder()
                    .setConnectTimeout(60 * 1000)
                    .setReadTimeout(60 * 1000)
                    .build()
            )
            if (BuildConfig.DEBUG) {
                //添加日志打印拦截器
                addInterceptor(LemonLogInterceptor(LemonLogLevel.BODY))
            }
        }
    }

    val service by lazy {
        lemon.create(ApiService::class.java)
    }

    private var token: String by Preference("token", "")

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    // Provide your custom parameter here
                    .appendQueryParams("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
                    .appendQueryParamsEcode("deviceModel", AppUtils.getMobileModel())
                    .build()
                return chain.proceed(newRequest)
            }
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    // Provide your custom header here
                    .setHeader("token", token)
                    .method(originalRequest.httpMethod, originalRequest.body)
                val request = requestBuilder.build()
                return chain.proceed(request)
            }
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                //#TODO Lemon 暂时未实现缓存机制
//            if (!NetworkUtil.isNetworkAvailable(ContextUtil.getContext())) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build()
//            }
                val response = chain.proceed(request)
                if (NetworkUtil.isNetworkAvailable(ContextUtil.getContext())) {
                    val maxAge = 0
                    // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
//                    response.newBuilder()//#TODO 暂时不支持Response newBuilder()
//                        .header("Cache-Control", "public, max-age=" + maxAge)
//                        .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                        .build()
                } else {
                    // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                    val maxStale = 60 * 60 * 24 * 28
//                    response.newBuilder()//#TODO 暂时不支持Response newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("nyn")
//                        .build()
                }
                return response
            }
        }
    }
}
