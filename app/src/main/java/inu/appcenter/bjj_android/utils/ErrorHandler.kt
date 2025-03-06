package inu.appcenter.bjj_android.utils

import org.json.JSONObject
import retrofit2.Response
import java.io.IOException


object ErrorHandler {
    fun handleApiError(response: Response<*>): AppError {
        return when (response.code()) {
            401 -> AppError.UnauthorizedError("인증이 필요합니다. 다시 로그인해주세요.")
            404 -> AppError.EmptyResponse("요청한 데이터를 찾을 수 없습니다.")
            in 400..499 -> {
                val errorMessage = parseErrorBody(response.errorBody()?.string())
                AppError.ApiError(errorMessage, response.code())
            }
            in 500..599 -> AppError.ApiError("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", response.code())
            else -> AppError.UnknownError("오류 코드: ${response.code()}")
        }
    }

    fun parseErrorBody(errorBody: String?): String {
        return try {
            errorBody?.let {
                // JSON 형식으로 파싱
                val jsonObject = JSONObject(it)
                // "msg" 필드가 배열인 경우
                if (jsonObject.has("msg") && jsonObject.get("msg") is org.json.JSONArray) {
                    val msgArray = jsonObject.getJSONArray("msg")
                    if (msgArray.length() > 0) {
                        return msgArray.getString(0)
                    }
                }
                // "msg" 필드가 문자열인 경우
                else if (jsonObject.has("msg")) {
                    return jsonObject.getString("msg")
                }
                // "code" 필드만 있는 경우
                else if (jsonObject.has("code")) {
                    return "오류 코드: ${jsonObject.getString("code")}"
                }

                "서버 오류가 발생했습니다"
            } ?: "서버 오류가 발생했습니다"
        } catch (e: Exception) {
            "서버 오류가 발생했습니다: ${e.message}"
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