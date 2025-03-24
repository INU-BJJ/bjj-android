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
                    } ?: emit(CustomResponse.Error(AppError.NotFoundError()))
                }
            } else {
                // 서버로부터 받은 에러 메시지 파싱 및 처리
                val error = ErrorHandler.handleApiError(response)
                emit(CustomResponse.Error(error))
            }
        } catch (e: IOException) {
            // 네트워크 오류 처리
            emit(CustomResponse.Error(ErrorHandler.handleNetworkError(e)))
        } catch (e: Exception) {
            // 알 수 없는 오류 처리
            emit(CustomResponse.Error(ErrorHandler.handleUnknownError(e)))
        }
    }
}