package com.picpay.desafio.android.di.datasource

import com.picpay.desafio.android.data.datasource.BaseDataSource
import com.picpay.desafio.android.data.datasource.UserDataSource
import com.picpay.desafio.android.data.datasource.UserDataSourceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val dataSourceModule = module {
    single { BaseDataSource }
    single<UserDataSource> {
        UserDataSourceImpl(service = get())
    }
}