package com.picpay.desafio.android.data.datasource

import com.picpay.desafio.android.data.utils.MemoryCache

abstract class BaseDataSource {
    companion object {
       const val USER = "user"
    }
    private var cache = MemoryCache()

    protected fun setCache(key: String, any: Any) = cache.set(key, any)
    protected fun getCache(key: String) = cache.get(key)
    protected fun removeCache(key: String) = cache.remove(key)
}