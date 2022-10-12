package com.picpay.desafio.android.viewmodels

import androidx.lifecycle.Observer
import com.picpay.desafio.android.MockModels
import com.picpay.desafio.android.MockitoProvideTest
import com.picpay.desafio.android.core.data.network.response.DataResponse
import com.picpay.desafio.android.core.data.network.response.Resource
import com.picpay.desafio.android.core.data.network.response.StatusResponse
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usercase.user.GetUsersUseCase
import com.picpay.desafio.android.presenter.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.*

@ExperimentalCoroutinesApi
class UserViewModelTest : MockitoProvideTest() {

    lateinit var viewModel: UserViewModel

    @Mock
    private lateinit var getUsersUseCase: GetUsersUseCase

    @Mock
    private lateinit var userObserver: Observer<DataResponse<List<User>>>

    @Captor
    private lateinit var userArgumentCaptor: ArgumentCaptor<DataResponse<List<User>>>

    @Before
    fun setUp() {
        viewModel = UserViewModel(getUsersUseCase).also {
            it.users.apply {
                observeForever(userObserver)
            }
        }
    }

    //region USER LIST
    @Test
    fun getUsers_whenStart_shouldReturnLoading() = scope.runTest {
        // given
        Mockito.`when`(getUsersUseCase.invoke(true)).thenReturn(
            flowOf(
                Resource.NotInitialized
            )
        )
        // when
        viewModel.getUsers(true)

        Mockito.verify(userObserver, Mockito.times(1)).onChanged(
            userArgumentCaptor.capture()
        )
        // then
        val values = userArgumentCaptor.allValues
        assertEquals(1, values.size)
        assertEquals(StatusResponse.LOADING, values.first().status)
    }

    @Test
    fun getUsers_whenIsSuccess_shouldReturnList() = scope.runTest {
        val list = MockModels.getUserList(2)
        Mockito.`when`(getUsersUseCase.invoke(true)).thenReturn(
            flowOf(
                Resource.Success(
                    list
                )
            )
        )

        viewModel.getUsers(true)

        Mockito.verify(userObserver, Mockito.times(1)).onChanged(
            userArgumentCaptor.capture()
        )

        val values = userArgumentCaptor.allValues
        assertEquals(1, values.size)
        assertEquals(StatusResponse.SUCCESS, values.first().status)
        assertEquals(list, values.first()?.data)
    }

    @Test
    fun getUsers_whenIsError_ShouldReturnFailure() = scope.runTest {
        Mockito.`when`(getUsersUseCase.invoke(true)).thenReturn(
            flowOf(
                Resource.Error(Exception())
            )
        )

        viewModel.getUsers(true)

        Mockito.verify(userObserver, Mockito.times(1)).onChanged(
            userArgumentCaptor.capture()
        )

        val values = userArgumentCaptor.allValues
        assertEquals(1, values.size)
        assertEquals(StatusResponse.FAILURE, values.first().status)
    }

    @Test
    fun getUsers_whenStartLoading_shouldReturnLoading() = scope.runTest {
        Mockito.`when`(getUsersUseCase.invoke(true)).thenReturn(
            flowOf(
                Resource.Loading
            )
        )

        viewModel.getUsers(true)

        Mockito.verify(userObserver, Mockito.times(1)).onChanged(
            userArgumentCaptor.capture()
        )

        val values = userArgumentCaptor.allValues
        assertEquals(1, values.size)
        assertEquals(StatusResponse.LOADING, values.first().status)
    }
    //endregion
}