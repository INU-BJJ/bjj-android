package inu.appcenter.bjj_android.utils

import retrofit2.Response
import java.io.IOException

object ErrorHandler {
    fun handleApiError(response: Response<*>): AppError {
        return when (response.code()) {
            401 -> AppError.UnauthorizedError("인증이 필요합니다. 다시 로그인해주세요.")
            404 -> AppError.EmptyResponse("요청한 데이터를 찾을 수 없습니다.")
            in 400..499 -> AppError.ApiError("요청에 문제가 있습니다: ${response.message()}", response.code())
            in 500..599 -> AppError.ApiError("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", response.code())
            else -> AppError.UnknownError("오류 코드: ${response.code()}")
        }
    }

    fun parseErrorBody(errorBody: String?): String {
        return try {
            // Try to extract message from error body
            errorBody?.let {
                if (it.contains("msg")) {
                    val regex = "\"msg\":\"(.*?)\"".toRegex()
                    val matchResult = regex.find(it)
                    matchResult?.groupValues?.get(1) ?: "서버 오류가 발생했습니다"
                } else {
                    "서버 오류가 발생했습니다"
                }
            } ?: "서버 오류가 발생했습니다"
        } catch (e: Exception) {
            "서버 오류가 발생했습니다"
        }
    }

    fun getUserFriendlyMessage(error: Throwable): String {
        // Convert any error to user-friendly message
        return when (error) {
            is AppError.NetworkError -> "네트워크 연결을 확인해주세요."
            is AppError.UnauthorizedError -> "로그인 후 이용해주세요."
            is AppError.EmptyResponse -> error.message
            is AppError.ApiError -> error.message
            is AppError.ValidationError -> error.message
            is AppError.UnknownError -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            is IOException -> "인터넷 연결을 확인해주세요."
            else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
        }
    }
}