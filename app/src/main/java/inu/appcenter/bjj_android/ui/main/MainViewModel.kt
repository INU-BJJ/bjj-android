package inu.appcenter.bjj_android.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TOKEN_DELAY = 300L
private const val RETRY_DELAY = 1000L
private const val MAX_RETRIES = 3

sealed class MainError : Exception() {
    data class EmptyResponse(override val message: String = "데이터가 비어있습니다.") : MainError()
    data class ApiError(override val message: String) : MainError()
    data class NetworkError(override val message: String) : MainError()
}

data class MainUiState(
    val cafeterias: List<String> = emptyList(),
    val selectedCafeteria: String? = null,
    val menus: List<TodayDietRes> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MainViewModel(
    private val cafeteriasRepository: CafeteriasRepository,
    private val todayDietRepository: TodayDietRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeAuthToken()
    }

    private fun observeAuthToken() {
        viewModelScope.launch {
            authViewModel.uiState
                .map { it.hasToken }
                .filterNotNull()
                .filter { it }
                .onEach { delay(TOKEN_DELAY) }
                .collect { getCafeterias() }
        }
    }

    fun selectCafeteria(cafeteria: String, forceRefresh: Boolean = false) {
        if (_uiState.value.selectedCafeteria == cafeteria && !forceRefresh) return

        _uiState.update {
            it.copy(
                selectedCafeteria = cafeteria,
                isLoading = true,
                error = null
            )
        }
        getMenusByCafeteria(cafeteria)
    }
    private suspend fun fetchCafeterias(retryCount: Int = 0) {
        try {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val response = cafeteriasRepository.getCafeterias()

            if (response.isSuccessful) {
                val cafeterias = response.body() ?: throw MainError.EmptyResponse("식당 정보가 비어있습니다.")
                _uiState.update {
                    it.copy(
                        cafeterias = cafeterias,
                        selectedCafeteria = it.selectedCafeteria ?: cafeterias.firstOrNull(),
                        isLoading = false
                    )
                }

                // 선택된 식당이 없을 때만 첫 번째 식당 선택
                if (_uiState.value.selectedCafeteria != null) {
                    getMenusByCafeteria(_uiState.value.selectedCafeteria!!)
                } else {
                    cafeterias.firstOrNull()?.let { firstCafeteria ->
                        getMenusByCafeteria(firstCafeteria)
                    }
                }
            } else {
                throw MainError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
            }
        } catch (e: Exception) {
            handleError(e, retryCount) { fetchCafeterias(it) }
        }
    }

    fun getCafeterias() {
        viewModelScope.launch {
            fetchCafeterias()
        }
    }

    private fun getMenusByCafeteria(cafeteriaName: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val response = todayDietRepository.getTodayDiet(cafeteriaName)

                if (response.isSuccessful) {
                    val menus = response.body() ?: throw MainError.EmptyResponse("식당 메뉴 정보가 비어있습니다.")
                    _uiState.update { it.copy(menus = menus, isLoading = false) }
                } else {
                    throw MainError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private suspend fun handleError(
        e: Exception,
        retryCount: Int = 0,
        retryOperation: suspend (Int) -> Unit = {}
    ) {
        Log.e("MainViewModel", "Error: ${e.message}", e)

        when {
            e.message?.contains("BEGIN_ARRAY") == true && retryCount < MAX_RETRIES -> {
                delay(RETRY_DELAY)
                retryOperation(retryCount + 1)
            }
            else -> {
                val errorMessage = when (e) {
                    is MainError.EmptyResponse -> e.message
                    is MainError.ApiError -> "API 오류: ${e.message}"
                    is MainError.NetworkError -> "네트워크 오류: ${e.message}"
                    else -> "알 수 없는 오류: ${e.message}"
                }
                _uiState.update { it.copy(isLoading = false, error = errorMessage) }
            }
        }
    }

    fun resetState() {
        _uiState.update { MainUiState() }
    }
}