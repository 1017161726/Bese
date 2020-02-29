package com.netlib

open class BaseResponse {
    var code: String? = null
    var status: String? = null
    var message: String? = null
    var success: Boolean? = null

    override fun toString(): String {
        return "BaseResponse(code=$code, status=$status, message=$message, success=$success)"
    }
}
