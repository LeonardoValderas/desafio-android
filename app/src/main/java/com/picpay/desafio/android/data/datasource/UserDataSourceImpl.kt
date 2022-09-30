package com.picpay.desafio.android.data.datasource

import com.picpay.desafio.android.core.data.network.adapter.NetworkResponse
import com.picpay.desafio.android.core.data.network.response.ApiResponseError
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.data.network.service.PicPayService
import com.picpay.desafio.android.data.network.response.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UserDataSourceImpl constructor(private val service: PicPayService): BaseDataSource(), UserDataSource {
    override suspend fun getFromRemote(): NetworkResponse<List<UserResponse>, ApiResponseError> {
        return service.getUsers()
    }

    override fun getFromLocal(): List<User>? {
        getCache(USER)?.let {
            return it as List<User>
        }

        return null
    }

    override fun saveLocal(users: List<User>?) {
        users?.let {
            setCache(USER, it)
        } ?: run {
            removeLocal()
        }
    }

    override fun removeLocal() {
        removeCache(USER)
    }
}