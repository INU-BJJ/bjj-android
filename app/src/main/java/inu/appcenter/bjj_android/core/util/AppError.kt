package inu.appcenter.bjj_android.core.util

sealed class AppError : Exception() {
    // 서버 오류
    data class ApiError(
        override val message: String,
        val statusCode: Int,
        val errorCode: String? = null
    ) : AppError()

    // 네트워크 오류
    data class NetworkError(
        override val message: String = "인터넷 연결을 확인해주세요."
    ) : AppError()

    // 인증 오류
    data class AuthError(
        override val message: String = "로그인이 필요합니다.",
        val isExpired: Boolean = false,
        val statusCode: Int = 401
    ) : AppError()

    // 유효성 검증 오류
    data class ValidationError(
        override val message: String,
        val field: String? = null
    ) : AppError()

    // 리소스 없음 오류
    data class NotFoundError(
        override val message: String = "요청한 데이터를 찾을 수 없습니다."
    ) : AppError()

    // 일반 오류
    data class GeneralError(
        override val message: String = "알 수 없는 오류가 발생했습니다."
    ) : AppError()

    // 서버 유지보수 오류
    data class MaintenanceError(
        override val message: String = "서버 점검 중입니다. 잠시 후 다시 시도해주세요."
    ) : AppError()

    companion object {
        // 오류 타입에 따른 사용자 친화적 메시지 생성
        fun getUserFriendlyMessage(error: Throwable): String {
            return when (error) {
                is NetworkError -> error.message
                is ApiError -> getApiErrorMessage(error)
                is AuthError -> error.message
                is ValidationError -> error.message
                is NotFoundError -> error.message
                is MaintenanceError -> error.message
                is GeneralError -> error.message
                else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            }
        }

        private fun getApiErrorMessage(error: ApiError): String {
            return when (error.statusCode) {
                401 -> "인증이 필요합니다. 다시 로그인해주세요."
                403 -> "접근 권한이 없습니다."
                404 -> "요청한 정보를 찾을 수 없습니다."
                500, 502, 503 -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                else -> error.message
            }
        }
    }
}