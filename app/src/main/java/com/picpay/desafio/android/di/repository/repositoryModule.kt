package com.picpay.desafio.android.di.repository

import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.data.repository.UserRepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single<UserRepositoryInterface> { UserRepository(dataSource = get()) }
}