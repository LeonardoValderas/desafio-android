package com.picpay.desafio.android.domain.usercase.user

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.picpay.desafio.android.MockModels
import com.picpay.desafio.android.base.BaseTest
import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class GetUsersTest: BaseTest() {

    private lateinit var getUsersUseCase: GetUsersUseCase
    @Mock
    private lateinit var repository: UserRepository

    override fun setUp() {
        super.setUp()
        getUsersUseCase = GetUsers(repository)
    }

    //region USERS
    @Test
    fun getUsers_whenIsRefresh_success_shouldReturnList() = runBlocking {
        val list = MockModels.getUserList(2)

        Mockito.`when`(repository.getUsers(true)).thenReturn(
            flowOf(Resource.Success(list))
        )

        getUsersUseCase = GetUsers(repository)

        val result = getUsersUseCase.invoke(true).toList()

        verify(repository, times(1)).getUsers(true)
        Assert.assertTrue(result[0] is Resource.Success)
        Assert.assertEquals(list, (result[0] as Resource.Success).data)
    }

    @Test
    fun getUsers_whenIsNotRefresh_success_shouldReturnList() = runBlocking {
        val list = MockModels.getUserList(2)

        Mockito.`when`(repository.getUsers(false)).thenReturn(
            flowOf(Resource.Success(list))
        )

        getUsersUseCase = GetUsers(repository)

        val result = getUsersUseCase.invoke(false).toList()

        verify(repository, times(1)).getUsers(false)
        Assert.assertTrue(result[0] is Resource.Success)
        Assert.assertEquals(list, (result[0] as Resource.Success).data)
    }

    @Test
    fun getUsers_whenIsNetworkError_shouldReturnSpecificError() = runBlocking {
        Mockito.`when`(repository.getUsers(true)).thenReturn(
            flowOf(Resource.Error(Exception()))
        )

        getUsersUseCase = GetUsers(repository)

        val result = getUsersUseCase.invoke(true).toList()

        Assert.assertTrue(result[0] is Resource.Error)
    }
    //endregion
}