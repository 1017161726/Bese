package com.bese.net


import com.bese.net.response.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit调用的API
 */
interface API {

    @POST("app/appUrl")
    fun requestAppBaseUrl(@Body body: RequestBody): Call<AppBaseUrlResponse>

}
