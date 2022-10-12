package com.picpay.desafio.android.data.repository

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.picpay.desafio.android.MockModels
import com.picpay.desafio.android.base.BaseTest
import com.picpay.desafio.android.core.data.network.adapter.NetworkResponse
import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.data.datasource.UserDataSource
import com.picpay.desafio.android.data.network.response.toUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.IOException

@ExperimentalCoroutinesApi
class UserRepositoryTest: BaseTest() {

    @Mock
    private lateinit var dataSource: UserDataSource
    private lateinit var repository: UserRepository

    override fun setUp(){
        super.setUp()
        repository = UserRepositoryImpl(dataSource)
    }

    //region USERS
    @Test
    fun getUsers_whenIsRefresh_success_shouldReturnFromRemote() = runBlocking {
        val list = MockModels.getUserResponseList(2)

        Mockito.`when`(dataSource.getFromRemote()).thenReturn(
                NetworkResponse.Success(list)
        )

        repository = UserRepositoryImpl(dataSource)

        val result = repository.getUsers(true).toList()

        verify(dataSource, times(0)).getFromLocal()
        verify(dataSource, times(1)).getFromRemote()
        assertEquals(Resource.Loading, result[0])
        assertTrue(result[1] is Resource.Success)
        assertEquals(list.map { it.toUser() }, (result[1] as Resource.Success).data)
    }

    @Test
    fun getUsers_whenIsNotRefresh_success_shouldReturnFromLocal() = runBlocking {
        val list = MockModels.getUserList(2)

        Mockito.`when`(dataSource.getFromLocal()).thenReturn(
            list
        )

        val result = repository.getUsers(false).toList()

        verify(dataSource, times(1)).getFromLocal()
        verify(dataSource, times(0)).getFromRemote()
        assertEquals(Resource.Loading, result[0])
        assertTrue(result[1] is Resource.Success)
        assertEquals(list, (result[1] as Resource.Success).data)
    }

    @Test
    fun getUsers_whenIsNotRefreshAndLocalNull_success_shouldReturnFromRemote() = runBlocking {
        val list = MockModels.getUserResponseList(2)

        Mockito.`when`(dataSource.getFromLocal()).thenReturn(
            null
        )

        Mockito.`when`(dataSource.getFromRemote()).thenReturn(
            NetworkResponse.Success(list)
        )

        val result = repository.getUsers(false).toList()

        verify(dataSource, times(1)).getFromLocal()
        verify(dataSource, times(1)).getFromRemote()
        assertEquals(Resource.Loading, result[0])
        assertTrue(result[1] is Resource.Success)
        assertEquals(list.map { it.toUser() }, (result[1] as Resource.Success).data)
    }

    @Test
    fun getUsers_whenIsNetworkError_shouldReturnSpecificError() = runBlocking {
        Mockito.`when`(dataSource.getFromRemote()).thenReturn(
            NetworkResponse.NetworkError(IOException())
        )

        val result = repository.getUsers(true).toList()

        assertEquals(Resource.Loading, result[0])
        assertTrue(result[1] is Resource.Error)
    }
    //endregion
}