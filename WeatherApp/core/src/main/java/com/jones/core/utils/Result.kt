package com.jones.core.utils

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Error(val exception: Throwable, val message: String? = null) : Result<Nothing>()

    data object Loading : Result<Nothing>()
}

fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

fun <T> Result<T>.isError(): Boolean = this is Result.Error

fun <T> Result<T>.isLoading(): Boolean = this is Result.Loading

fun <T> Result<T>.getOrNull(): T? =
    when (this) {
        is Result.Success -> data
        else -> null
    }

