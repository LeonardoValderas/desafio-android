package com.picpay.desafio.android.core.data.network.response

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>() {
        override fun succeeded(): Boolean {
            return true
        }
    }
    data class Error(val exception: Throwable) : Resource<Nothing>()
    object NotInitialized : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    open fun succeeded(): Boolean {
        return false
    }
}