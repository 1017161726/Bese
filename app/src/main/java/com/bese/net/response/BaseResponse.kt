package com.bese.net.response

/**
 * API规范响应实体类基类, 业务返回的Response都继承此类
 */
open class BaseResponse {
    var code: String? = null
    var message: String? = null
    var success: Boolean? = null
}

data class AppBaseUrlResponse(var data: AppBaseUrlData?) : BaseResponse()
