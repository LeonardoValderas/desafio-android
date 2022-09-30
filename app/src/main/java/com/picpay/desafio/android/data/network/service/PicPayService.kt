package com.picpay.desafio.android.data.network.service

import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.core.data.network.Retrofit
import com.picpay.desafio.android.core.data.network.adapter.NetworkResponse
import com.picpay.desafio.android.core.data.network.response.ApiResponseError
import com.picpay.desafio.android.data.network.response.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.http.GET

@ExperimentalCoroutinesApi
interface PicPayService {
    @GET("users")
    suspend fun getUsers(): NetworkResponse<List<UserResponse>, ApiResponseError>

    companion object {
        operator fun invoke(url: String = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"): PicPayService {
            return Retrofit
                .invoke(url, BuildConfig.DEBUG)
                .create(PicPayService::class.java)
        }
    }
}