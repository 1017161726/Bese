package com.netlib

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response

/**
 * OkHttp请求基类
 */
abstract class BaseRequest<T, R>(private var TAG: Any? = null) {

    private var mParam: T? = null
    private var mCall: Call<R>? = null
    private val mDispatcher = APICreator.client?.dispatcher

    /**
     * 获取需要请求的Call对象
     */
    abstract fun getCall(): Call<R>

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

    /**
     * 获取RequestBody默认实现
     */
    fun getRequestBody(): RequestBody {
        return (mParam?.run { Gson().toJson(this) } ?: JsonObject().toString()).toRequestBody("application/json".toMediaTypeOrNull())
    }

    /**
     * 异步请求
     */
    fun request(param: T?, callback: BaseCallback<R>) {
        mParam = param
        mCall = getCall()
        TAG?.run { mCall?.request()?.newBuilder()?.tag(this) }

        /** 进队 开始异步请求 */
        mCall?.enqueue(callback)
    }

    /**
     * 同步请求
     */
    fun requestSync(param: T?): Response<R>? {
        mParam = param
        mCall = getCall()
        TAG?.run { mCall?.request()?.newBuilder()?.tag(this) }
        /** 进队 开始同步请求 */
        return mCall?.execute()
    }

}