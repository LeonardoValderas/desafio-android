package com.picpay.desafio.android.data.datasource

import com.nhaarman.mockitokotlin2.any
import com.picpay.desafio.android.MockModels.getUserList
import com.picpay.desafio.android.MockModels.getUserResponseList
import com.picpay.desafio.android.base.BaseTest
import com.picpay.desafio.android.core.data.network.adapter.NetworkResponse
import com.picpay.desafio.android.core.data.network.response.ApiResponseError
import com.picpay.desafio.android.data.network.response.UserResponse
import com.picpay.desafio.android.data.network.service.PicPayService
import com.picpay.desafio.android.data.utils.MemoryCache
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.IOException

@ExperimentalCoroutinesApi
class UserDataSourceTest: BaseTest() {
    @Mock
    private lateinit var service: PicPayService

    @Mock
    private lateinit var cache: MemoryCache
    private lateinit var dataSource: UserDataSource

    override fun setUp() {
        super.setUp()
        dataSource = UserDataSourceImpl(service, cache)
    }

    //region FROM REMOTE SERVICE
    @Test
    fun getUsers_whenIsSuccess_shouldReturnUserResponseList() = runBlocking {
        val list = getUserResponseList(2)

        Mockito.`when`(service.getUsers()).thenReturn(
            NetworkResponse.Success(list)
        )

        val result: NetworkResponse<List<UserResponse>, ApiResponseError> =
            dataSource.getFromRemote()
        assertTrue(result is NetworkResponse.Success)
        assertEquals(list, (result as NetworkResponse.Success).body)
    }

    @Test
    fun getUsers_whenIsNetworkError_shouldReturnSpecificError() = runBlocking {
        Mockito.`when`(service.getUsers()).thenReturn(
            NetworkResponse.NetworkError(IOException())
        )

        val result: NetworkResponse<List<UserResponse>, ApiResponseError> =
            dataSource.getFromRemote()
        assertTrue(result is NetworkResponse.NetworkError)
    }

    @Test
    fun getUsers_whenIsUnknownError_shouldReturnSpecificError() = runBlocking {
        Mockito.`when`(service.getUsers()).thenReturn(
            NetworkResponse.UnknownError(Throwable())
        )

        val result: NetworkResponse<List<UserResponse>, ApiResponseError> =
            dataSource.getFromRemote()
        assertTrue(result is NetworkResponse.UnknownError)
    }

    @Test
    fun getUsers_whenIsTokenError_shouldReturnSpecificError() = runBlocking {
        Mockito.`when`(service.getUsers()).thenReturn(
            NetworkResponse.TokenError(Throwable())
        )

        val result: NetworkResponse<List<UserResponse>, ApiResponseError> =
            dataSource.getFromRemote()
        assertTrue(result is NetworkResponse.TokenError)
    }
    //endregion

    //region FROM LOCAL
    @Test
    fun getUsers_whenIsSuccess_shouldReturnUserList() = runBlocking {
        val list = getUserList(2)

        Mockito.`when`(cache.get(any())).thenReturn(
            list
        )

        val result: List<User>? = dataSource.getFromLocal()
        assertEquals(list, result)
    }

    @Test
    fun getUsers_whenIsEmpty_shouldReturnNull() = runBlocking {
        Mockito.`when`(cache.get(any())).thenReturn(
            null
        )

        val result: List<User>? = dataSource.getFromLocal()
        assertNull(result)
    }
    //endregion


    @After
    fun tearDown() {

    }
}