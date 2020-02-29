package com.ved.net.response

import com.netlib.BaseResponse

data class AppBaseUrlResponse(var data: AppBaseUrlData) : BaseResponse()
data class CheckUpdateResponse(var data: CheckUpdateData) : BaseResponse()