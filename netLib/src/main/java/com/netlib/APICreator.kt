package com.netlib

import com.netlib.config.NetConfig
import com.netlib.tool.GsonConverterFactory
import com.netlib.tool.HeaderInterceptor
import com.netlib.tool.TrustAllHttps
import com.netlib.tool.UserAgentInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * 网络请求API
 */
object APICreator {

    var client: OkHttpClient? = null
        private set

    /**
     * 网络框架 Retrofit
     * 在使用网络时，iss = true 代表需要兼容HTTPS , iss = false 代表不用兼容HTTPS
     */
    val retrofit: Retrofit
        get() {
            val base: String
            val iss: Boolean
            if (true) {
                base = NetConfig.BASE_URL
                iss = true
            } else {
                base = NetConfig.BASE_URL
                iss = false
            }
            return getRetrofit(base, iss)
        }

    private fun getRetrofit(base: String, iss: Boolean): Retrofit {
        val clientBuilder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        client = clientBuilder
            .retryOnConnectionFailure(true)
            .connectTimeout(NetConfig.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(NetConfig.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(NetConfig.RESPONSE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(UserAgentInterceptor())
            .addInterceptor(httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        if (iss) {
            TrustAllHttps.createSSLSocketFactory()?.let { clientBuilder.sslSocketFactory(it, TrustAllHttps.TrustAllCerts()) }
            clientBuilder.hostnameVerifier(TrustAllHttps.TrustAllHostnameVerifier())
            client = clientBuilder.build()
        }
        // 创建Retrofit对象
        return Retrofit.Builder()
            .baseUrl(base)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}