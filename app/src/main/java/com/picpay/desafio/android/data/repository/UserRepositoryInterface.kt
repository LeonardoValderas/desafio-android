package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.utils.Resource
import com.picpay.desafio.android.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepositoryInterface {
    fun getUsers(isRefresh: Boolean): Flow<Resource<List<User>>>
}