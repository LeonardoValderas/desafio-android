package com.picpay.desafio.android.domain.usercase.user

import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCase {
    operator fun invoke(isRefresh: Boolean): Flow<Resource<List<User>?>>
}