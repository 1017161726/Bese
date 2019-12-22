package com.bese.net

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response

/**
 * 网络请求管理类
 */
object RequestManager {
    private val mOkHttpClient: OkHttpClient? = APICreator.client
    private val mDispatcher = mOkHttpClient?.dispatcher
    val mAPI: API = APICreator.default

    /**
     * 进队 开始异步请求
     */
    fun <R> enqueue(call: Call<R>?, callback: BaseCallback<R>) {
        call?.enqueue(callback)
    }

    /**
     * 同步请求
     * @param call Call<R>
     * @return Response<R>
     */
    fun <R> execute(call: Call<R>?): Response<R>? {
        return call?.execute()
    }

    /**
     * 根据Tag取消请求    OkHttp3.X以后 默认会把当前Request设置为Tag
     * @param tag Any
     */
    fun cancel(tag: Any? = null) {
        mDispatcher?.run {
            for (queuedCall in queuedCalls()) {
                if (tag == queuedCall.request().tag()) {
                    queuedCall.cancel()
                }
            }

            for (runningCall in runningCalls()) {
                if (tag == runningCall.request().tag()) {
                    runningCall.cancel()
                }
            }
        }
    }

    /**
     * 取消所有请求
     */
    fun cancelAll() {
        mDispatcher?.cancelAll()
    }

}