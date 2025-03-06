package inu.appcenter.bjj_android.repository.base

import inu.appcenter.bjj_android.utils.AppError
import inu.appcenter.bjj_android.utils.CustomResponse
import inu.appcenter.bjj_android.utils.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException


interface BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<CustomResponse<T>> = flow {
        emit(CustomResponse.Loading())
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                if (response.body() == null && response.code() == 204) {
                    // 204 No Content와 같이 응답 본문이 없어도 정상적인 경우 처리
                    emit(CustomResponse.Success(Unit as T))
                } else {
                    response.body()?.let { body ->
                        emit(CustomResponse.Success(body as T))
                    } ?: emit(CustomResponse.Error(AppError.EmptyResponse()))
                }
            } else {
                // 서버로부터 받은 에러 메시지 파싱
                val errorMessage = ErrorHandler.parseErrorBody(response.errorBody()?.string())
                val apiError = AppError.ApiError(errorMessage, response.code())
                emit(CustomResponse.Error(apiError))
            }
        } catch (e: IOException) {
            emit(CustomResponse.Error(AppError.NetworkError("네트워크 연결 실패: ${e.message}")))
        } catch (e: Exception) {
            emit(CustomResponse.Error(AppError.UnknownError(e.message ?: "알 수 없는 오류")))
        }
    }
}