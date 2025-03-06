package inu.appcenter.bjj_android.utils

sealed class AppError : Exception() {
    data class EmptyResponse(override val message: String = "데이터가 비어있습니다.") : AppError()
    data class ApiError(override val message: String, val statusCode: Int? = null) : AppError()
    data class NetworkError(override val message: String) : AppError()
    data class ValidationError(override val message: String) : AppError()
    data class UnauthorizedError(override val message: String = "로그인이 필요합니다.") : AppError()
    data class UnknownError(override val message: String = "알 수 없는 오류가 발생했습니다.") : AppError()
}