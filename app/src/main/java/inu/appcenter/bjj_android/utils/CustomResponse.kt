package inu.appcenter.bjj_android.utils

sealed class CustomResponse<T> {
    class Success<T>(val data:T): CustomResponse<T>()
    class Error<T>(val error:Throwable?,val data:T? = null): CustomResponse<T>()
    class Loading<T>: CustomResponse<T>()
}