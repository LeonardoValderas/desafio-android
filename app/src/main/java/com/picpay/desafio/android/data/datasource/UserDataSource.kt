package com.picpay.desafio.android.data.datasource

import com.picpay.desafio.android.core.data.network.adapter.NetworkResponse
import com.picpay.desafio.android.core.data.network.response.ApiResponseError
import com.picpay.desafio.android.data.network.response.UserResponse
import com.picpay.desafio.android.domain.model.User

interface UserDataSource {
    suspend fun getFromRemote(): NetworkResponse<List<UserResponse>, ApiResponseError>
    fun getFromLocal(): List<User>?
    fun saveLocal(users: List<User>?)
    fun removeLocal()
}