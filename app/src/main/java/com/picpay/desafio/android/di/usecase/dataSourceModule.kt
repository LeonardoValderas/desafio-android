package com.picpay.desafio.android.di.usecase

import com.picpay.desafio.android.domain.usercase.user.GetUsers
import com.picpay.desafio.android.domain.usercase.user.GetUsersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val useCaseModule = module {
    single<GetUsersUseCase> {
        GetUsers(repository = get())
    }
}