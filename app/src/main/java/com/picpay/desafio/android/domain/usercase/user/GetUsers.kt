package com.picpay.desafio.android.domain.usercase.user

import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetUsers(private val repository: UserRepository): GetUsersUseCase {
    override fun invoke(isRefresh: Boolean): Flow<Resource<List<User>?>> = try {
        repository.getUsers(isRefresh)
    } catch (e: Exception) {
        flowOf(Resource.Error(e))
    }
}