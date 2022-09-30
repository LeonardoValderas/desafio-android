package com.picpay.desafio.android.core.data.network.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseError(
    val error: String,
    val message: String){
    constructor(): this("status", "Error")
}