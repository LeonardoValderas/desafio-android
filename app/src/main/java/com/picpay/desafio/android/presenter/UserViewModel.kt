package com.picpay.desafio.android.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.core.data.network.response.DataResponse
import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.domain.usercase.user.GetUsersUseCase
import com.picpay.desafio.android.utils.OpenForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@OpenForTesting
class UserViewModel(private val getUsersUseCase: GetUsersUseCase): ViewModel() {
    private val _users = MutableLiveData<DataResponse<List<User>>>()
    val users: LiveData<DataResponse<List<User>>>
        get() = _users

//    init {
//        getUsers(false)
//    }

    final fun getUsers(isRefresh: Boolean) {
        try {
            getUsersUseCase.invoke(isRefresh)
                .flowOn(Dispatchers.IO)
                .map {
                    when (it) {
                        is Resource.NotInitialized, Resource.Loading -> {
                            _users.value = DataResponse.LOADING()
                        }
                        is Resource.Success -> {
                            _users.value = DataResponse.SUCCESS(it.data)
                        }
                        is Resource.Error -> {
                            _users.value =
                                DataResponse.FAILURE()
                        }
                    }
                }.launchIn(viewModelScope)
        } catch (e: Exception) {
            _users.value = DataResponse.FAILURE()
        }
    }
}