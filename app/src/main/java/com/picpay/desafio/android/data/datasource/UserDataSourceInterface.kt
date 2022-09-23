package com.picpay.desafio.android.data.datasource

import com.picpay.desafio.android.data.network.reponses.ApiResponse
import com.picpay.desafio.android.model.User
import kotlinx.coroutines.flow.Flow

interface UserDataSourceInterface {
    fun getFromRemote(): Flow<ApiResponse<List<User>>>
    fun getFromLocal(): Flow<List<User>?>
    fun saveLocal(users: List<User>?)
    fun removeLocal()
}