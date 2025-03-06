package inu.appcenter.bjj_android.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.utils.AppError
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class BaseViewModel : ViewModel() {
    private val _errorEvent = MutableSharedFlow<Throwable>()
    val errorEvent = _errorEvent.asSharedFlow()

    // MutableSharedFlow에서 MutableStateFlow로 변경
    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    protected fun <T> Flow<CustomResponse<T>>.handleResponse(
        onError: suspend (Throwable) -> Unit = { emitError(it) },
        onLoading: suspend () -> Unit = { setLoading(true) },
        onSuccess: suspend (T) -> Unit
    ) = viewModelScope.launch {
        collect { response ->
            when (response) {
                is CustomResponse.Loading -> onLoading()
                is CustomResponse.Success -> {
                    setLoading(false)
                    onSuccess(response.data as T)
                }
                is CustomResponse.Error -> {
                    setLoading(false)
                    onError(response.error)
                }
            }
        }
    }

    protected fun emitError(error: Throwable) {
        viewModelScope.launch {
            _errorEvent.emit(error)
        }
    }

    // setLoading 메서드 수정 - 이제 value를 사용
    protected fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }
}