package com.bese.net.request

import com.bese.net.BaseRequest
import com.bese.net.response.AppBaseUrlResponse
import retrofit2.Call

/**
 * APP域名更换接口
 */
class AppBaseUrlRequest : BaseRequest<Any, AppBaseUrlResponse>() {
    override fun getCall(): Call<AppBaseUrlResponse> {
        return mAPI.requestAppBaseUrl(getRequestBody())
    }

}
