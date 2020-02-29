package com.ved.net.request

import com.netlib.BaseRequest
import com.ved.net.NetTool
import com.ved.net.response.AppBaseUrlResponse
import retrofit2.Call

/**
 * @author Endless
 * @date 2019-08-27
 * desc: APP域名更换接口
 */
class AppBaseUrlRequest : BaseRequest<Any, AppBaseUrlResponse>() {
    override fun getCall(): Call<AppBaseUrlResponse> {
        return NetTool.getApi().requestAppBaseUrl(getRequestBody())
    }
    data class Param(var type: String?)
}
