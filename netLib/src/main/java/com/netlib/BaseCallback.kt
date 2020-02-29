package com.netlib

import com.blankj.utilcode.util.ToastUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * 回调基类
 */
abstract class BaseCallback<T>(private var toastFlag: Boolean = true) : Callback<T> {
    enum class Exception(var desc: String) {
        Business("业务异常"),
        DataStructure("数据结构异常"),
        ResponseCode("响应码异常"),
        Network("网络异常"),
        Unhandled("未处理异常")
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        // retrofit2根据响应码是否在区间[200, 300)判断响应是否成功
        if (response.isSuccessful) {
            // 检查数据结构是否符合约定
            if (response.body() is BaseResponse) {
                // 根据success字段判断业务是否成功
                if ((response.body() as BaseResponse).success == true) {
                    onSuccess(response.body())
                } else {
                    onException(Exception.Business, response.body())
                }
            } else {
                onException(Exception.DataStructure, response.body())
            }
        } else {
            onException(Exception.ResponseCode, response.errorBody())
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (t is ConnectException || t is UnknownHostException || t is HttpException || t is InterruptedIOException) {
            onException(Exception.Network, t)
        } else {
            onException(Exception.Unhandled, t)
        }
    }


    /** 业务正常期望回调 */
    abstract fun onSuccess(response: T?)

    /** 异常回调 转发 */
    @Suppress("UNCHECKED_CAST")
    open fun onException(exception: Exception, content: Any?) {
        when (exception) {
            Exception.Business -> onBusinessException(exception, content as T?)
            Exception.DataStructure -> onDataStructureException(exception, content as T?)
            Exception.ResponseCode -> onResponseCodeException(exception, content as ResponseBody?)
            Exception.Network -> onNetworkException(exception, content as Throwable)
            Exception.Unhandled -> onUnhandledException(exception, content as Throwable)
        }
    }

    /** 业务异常 */
    open fun onBusinessException(exception: Exception, response: T?) {
        // 通用处理业务异常
        (response as? BaseResponse)?.code?.run { }
        if (toastFlag) response.run { ToastUtils.showShort((response as? BaseResponse)?.message) }
    }

    /** 数据结构异常 */
    open fun onDataStructureException(exception: Exception, response: T?) {
        if (toastFlag) ToastUtils.showShort(exception.desc.plus("：\n").plus(response?.toString()))
    }

    /** 响应码异常    正常范围为[200..300) */
    open fun onResponseCodeException(exception: Exception, errorBody: ResponseBody?) {
        if (toastFlag) ToastUtils.showShort("服务器异常，请稍后再试")
    }

    /** 网络异常 */
    open fun onNetworkException(exception: Exception, t: Throwable) {
        if (toastFlag) ToastUtils.showShort(exception.desc.plus("，请检查网络"))
    }

    /** 未处理异常 */
    open fun onUnhandledException(exception: Exception, t: Throwable) {
        if (toastFlag) ToastUtils.showShort(exception.desc.plus("：\n").plus(t.localizedMessage))
    }
}
