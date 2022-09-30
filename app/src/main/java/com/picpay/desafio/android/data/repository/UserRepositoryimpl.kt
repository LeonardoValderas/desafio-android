package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.core.data.network.adapter.NetworkResponse
import com.picpay.desafio.android.core.data.network.error.NetworkException
import com.picpay.desafio.android.core.data.network.error.UnknownException
import com.picpay.desafio.android.core.data.network.response.ApiResponseError
import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.data.datasource.UserDataSource
import com.picpay.desafio.android.data.network.response.UserResponse
import com.picpay.desafio.android.data.network.response.toUser
import com.picpay.desafio.android.utils.OpenForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@OpenForTesting
class UserRepositoryImpl(private val dataSource: UserDataSource) : UserRepository {
    override fun getUsers(isRefresh: Boolean): Flow<Resource<List<User>?>> = flow {
        try {
            emit(Resource.Loading)
            if (isRefresh) {
                emitAll(flowOf(getResourceFromRemote(dataSource.getFromRemote())))
            } else {
                dataSource.getFromLocal()?.let { localData ->
                    emit(Resource.Success(localData))
                } ?: kotlin.run {
                    emitAll(flowOf(getResourceFromRemote(dataSource.getFromRemote())))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    private fun getResourceFromRemote(remoteData: NetworkResponse<List<UserResponse>, ApiResponseError>): Resource<List<User>> {
        return when (remoteData) {
            is NetworkResponse.Success -> {
                Resource.Success(remoteData.body?.map { it.toUser() } ?: mutableListOf())
            }
            is NetworkResponse.ApiError -> {
                Resource.Error(Exception(remoteData.body.error))
            }
            is NetworkResponse.NetworkError -> {
                Resource.Error(NetworkException())
            }
            is NetworkResponse.UnknownError -> {
                Resource.Error(UnknownException())
            }
            else -> {
                Resource.Error(UnknownException())
            }
        }
    }
}