package com.picpay.desafio.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
abstract class MockitoProvideTest {

    protected val dispatcher = UnconfinedTestDispatcher()
    protected val scope = TestScope(dispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initMocks() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDownMocks() {
        Dispatchers.resetMain()
    }

    fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}