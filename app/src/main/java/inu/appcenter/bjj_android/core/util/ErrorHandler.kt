package inu.appcenter.bjj_android.core.util

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException


object ErrorHandler {

    fun handleApiError(response: Response<*>): AppError {
        return when (response.code()) {
            401 -> AppError.AuthError(isExpired = true)
            403 -> AppError.AuthError(message = "접근 권한이 없습니다.")
            404 -> AppError.NotFoundError()
            500, 502, 503 -> AppError.ApiError(
                message = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                statusCode = response.code()
            )
            503 -> AppError.MaintenanceError()
            in 400..499 -> {
                // 클라이언트 오류
                val errorBody = parseErrorBody(response.errorBody()?.string())
                val errorCode = parseErrorCode(response.errorBody()?.string())
                AppError.ApiError(
                    message = errorBody,
                    statusCode = response.code(),
                    errorCode = errorCode
                )
            }
            else -> AppError.ApiError(
                message = "알 수 없는 오류가 발생했습니다. (${response.code()})",
                statusCode = response.code()
            )
        }
    }

    fun handleNetworkError(error: IOException): AppError.NetworkError {
        return AppError.NetworkError()
    }

    fun handleUnknownError(error: Throwable): AppError.GeneralError {
        return AppError.GeneralError(
            message = error.message ?: "알 수 없는 오류가 발생했습니다."
        )
    }

    private fun parseErrorBody(errorBody: String?): String {
        return try {
            errorBody?.let {
                val jsonObject = JSONObject(it)
                if (jsonObject.has("msg")) {
                    if (jsonObject.get("msg") is JSONArray) {
                        val msgArray = jsonObject.getJSONArray("msg")
                        if (msgArray.length() > 0) {
                            return msgArray.getString(0)
                        }
                    } else {
                        return jsonObject.getString("msg")
                    }
                } else if (jsonObject.has("message")) {
                    return jsonObject.getString("message")
                } else if (jsonObject.has("error")) {
                    return jsonObject.getString("error")
                }
                "서버 오류가 발생했습니다"
            } ?: "서버 오류가 발생했습니다"
        } catch (e: Exception) {
            "서버 오류가 발생했습니다"
        }
    }

    private fun parseErrorCode(errorBody: String?): String? {
        return try {
            errorBody?.let {
                val jsonObject = JSONObject(it)
                if (jsonObject.has("code")) {
                    jsonObject.getString("code")
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}