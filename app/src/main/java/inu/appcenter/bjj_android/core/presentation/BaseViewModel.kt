package inu.appcenter.bjj_android.core.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.core.util.AppError
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel : ViewModel() {
    private val _errorEvent = MutableSharedFlow<Throwable>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    // 토스트 메시지 이벤트
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    // 인증 만료 이벤트
    private val _authExpiredEvent = MutableSharedFlow<Unit>()
    val authExpiredEvent = _authExpiredEvent.asSharedFlow()

    // API 응답 처리하는 공통 함수
    protected fun <T> Flow<CustomResponse<T>>.handleResponse(
        showErrorToast: Boolean = true,
        handleLoading: Boolean = true,
        onError: suspend (Throwable) -> Unit = { handleError(it, showErrorToast) },
        onLoading: suspend () -> Unit = { if (handleLoading) setLoading(true) },
        onSuccess: suspend (T) -> Unit
    ) = viewModelScope.launch {
        collect { response ->
            when (response) {
                is CustomResponse.Loading -> onLoading()
                is CustomResponse.Success -> {
                    if (handleLoading) setLoading(false)
                    onSuccess(response.data as T)
                }
                is CustomResponse.Error -> {
                    if (handleLoading) setLoading(false)
                    onError(response.error)
                }
            }
        }
    }

    // 오류 처리 함수
    protected suspend fun handleError(error: Throwable, showToast: Boolean = true) {
        _errorEvent.emit(error)

        // 인증 오류인 경우 별도 이벤트 발생
        if (error is AppError.AuthError) {
            if (error.isExpired || error.statusCode == 401) {  // 만료 또는 인증 오류
                _authExpiredEvent.emit(Unit)
            }
        }

        // 토스트 메시지 표시 설정이 켜진 경우
        if (showToast) {
            val message = getErrorMessage(error)
            _toastEvent.emit(message)
        }
    }

    // 직접 오류 이벤트 발생시키는 함수
    protected fun emitError(error: Throwable) {
        viewModelScope.launch {
            handleError(error)
        }
    }

    // 로딩 상태 설정 함수
    protected fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }

    // 토스트 메시지 발생 함수
    protected fun showToast(message: String) {
        viewModelScope.launch {
            _toastEvent.emit(message)
        }
    }

    // 오류 메시지 생성 함수
    private fun getErrorMessage(error: Throwable): String {
        return when (error) {
            is AppError -> {
                when (error) {
                    is AppError.ApiError -> getApiErrorMessage(error)
                    is AppError.NetworkError -> error.message
                    is AppError.AuthError -> error.message
                    is AppError.ValidationError -> error.message
                    is AppError.NotFoundError -> error.message
                    is AppError.MaintenanceError -> error.message
                    is AppError.GeneralError -> error.message
                }
            }
            else -> error.message ?: "알 수 없는 오류가 발생했습니다."
        }
    }

    // API 오류 메시지 변환 함수
    private fun getApiErrorMessage(error: AppError.ApiError): String {
        return when (error.statusCode) {
            401 -> "인증이 필요합니다. 다시 로그인해주세요."
            403 -> "접근 권한이 없습니다."
            404 -> "요청한 정보를 찾을 수 없습니다."
            500, 502, 503 -> "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
            else -> error.message
        }
    }
}