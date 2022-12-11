package com.holudi.moviewatchlist.data

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val throwable: Throwable) : Resource<T>()
}
