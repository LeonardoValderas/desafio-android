package com.picpay.desafio.android.di.network

import com.picpay.desafio.android.data.network.service.PicPayService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val networkModule = module {
    single {
        PicPayService()
    }
}