package com.netlib.tool

import okhttp3.Interceptor
import okhttp3.Response


/**
 * <> 请求头配置 </>
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =chain.request().newBuilder()
                .addHeader("version", "v1")
                .addHeader("sid", getToken().orEmpty())
                .build()
        return chain.proceed(request)
    }

    private fun getToken(): String? {
        return ""
    }

}