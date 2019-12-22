package com.bese.net

import android.os.Build
import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * <> UserAgent 拦截器 </>
 */
class UserAgentInterceptor : Interceptor {

    private var userAgent: String? = null

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
                .addHeader("user-agent", getUserAgent())
                .addHeader("device", getDeviceInfo())
                .build()
        return chain.proceed(request)
    }

    fun setUserAgent(agent: String?) {
        agent?.run { userAgent = this }
    }

    private fun getUserAgent(): String {
        return if (!TextUtils.isEmpty(userAgent)) {
            userAgent ?: ""
        } else {
            "andr"
        }
    }

    private fun getDeviceInfo(): String {
        return Build.VERSION.RELEASE + ";" + Build.BRAND + ";" + Build.MODEL + ";V" + AppUtils.getAppVersionCode()
    }

}
