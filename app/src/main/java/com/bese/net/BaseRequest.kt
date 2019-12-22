package com.bese.net

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response

/**
 * desc: OkHttp请求基类
 */
abstract class BaseRequest<T, R>(private var TAG: Any? = null) {

    private var manager: RequestManager = RequestManager
    protected val mAPI = manager.mAPI
    private var mParam: T? = null
    private var mCall: Call<R>? = null

    /**
     * 获取需要请求的Call对象
     */
    abstract fun getCall(): Call<R>

    /**
     * 取消请求
     */
    fun cancel(tag: Any? = null) {
        manager.cancel(tag)
    }

    /**
     * 取消所有请求
     */
    fun cancelAll() {
        manager.cancelAll()
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
        TAG?.run { mCall?.request()?.newBuilder()?.tag(this) }      // 设置自定义标签
        manager.enqueue(mCall, callback)
    }

    /**
     * 同步请求
     */
    fun requestSync(param: T?): Response<R>? {
        mParam = param
        mCall = getCall()
        TAG?.run { mCall?.request()?.newBuilder()?.tag(this) }      // 设置自定义标签
        return manager.execute(mCall)
    }

}