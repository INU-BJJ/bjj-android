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
                response.body()?.let {
                    emit(CustomResponse.Success(it))
                } ?: emit(CustomResponse.Error(AppError.EmptyResponse()))
            } else {
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
