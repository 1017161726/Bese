package com.bese.net

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * desc: Retrofit Gson转换工厂
 */
class GsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }

    companion object {
        fun create(gson: Gson): GsonConverterFactory {
            return GsonConverterFactory(gson)
        }

        fun create(): GsonConverterFactory {
            return create(GsonBuilder()
//                    Java类Number(不是kotlin Int Float等父类，所以需要分开写)
                    .registerTypeHierarchyAdapter(Number::class.java, object : JsonDeserializer<Number> {
                        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Number {
                            return try {
                                Gson().fromJson(json, typeOfT)
                            } catch (e: JsonSyntaxException) {
                                0
                            }
                        }
                    })
//                    Kotlin类Int
                    .registerTypeAdapter(Int::class.java, object : JsonDeserializer<Int> {
                        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Int {
                            return try {
                                Gson().fromJson(json, typeOfT)
                            } catch (e: JsonSyntaxException) {
                                0
                            }
                        }
                    })
//                    Kotlin类Float
                    .registerTypeAdapter(Float::class.java, object : JsonDeserializer<Float> {
                        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Float {
                            return try {
                                Gson().fromJson(json, typeOfT)
                            } catch (e: JsonSyntaxException) {
                                0f
                            }
                        }
                    })
//                    列表    可能会使整个列表为空
//                    .registerTypeHierarchyAdapter(List::class.java, object : JsonDeserializer<List<*>> {
//                        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): List<*> {
//                            return try {
//                                Gson().fromJson(json, typeOfT)
//                            } catch (e: JsonSyntaxException) {
//                                emptyList<Any>()
//                            }
//                        }
//                    })
                    .create())
        }
    }
}