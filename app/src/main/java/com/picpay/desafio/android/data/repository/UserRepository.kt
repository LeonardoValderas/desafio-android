package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(isRefresh: Boolean): Flow<Resource<List<User>?>>
}