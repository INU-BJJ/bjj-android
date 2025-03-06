package inu.appcenter.bjj_android.ui.main
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepository
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.utils.AppError
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
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

data class MainUiState(
    val cafeterias: List<String> = emptyList(),
    val selectedCafeteria: String? = null,
    val menus: List<TodayDietRes> = emptyList()
)

class MainViewModel(
    private val cafeteriasRepository: CafeteriasRepository,
    private val todayDietRepository: TodayDietRepository,
    private val authViewModel: AuthViewModel,
    private val menuRepository: MenuRepository
) : BaseViewModel() {

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
                selectedCafeteria = cafeteria
            )
        }
        getMenusByCafeteria(cafeteria)
    }

    private suspend fun fetchCafeterias(retryCount: Int = 0) {
        cafeteriasRepository.getCafeterias().handleResponse(
            onSuccess = { cafeterias ->
                _uiState.update {
                    it.copy(
                        cafeterias = cafeterias,
                        selectedCafeteria = it.selectedCafeteria ?: cafeterias.firstOrNull()
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
            },
            onError = { error ->
                if (error.message?.contains("BEGIN_ARRAY") == true && retryCount < MAX_RETRIES) {
                    delay(RETRY_DELAY)
                    fetchCafeterias(retryCount + 1)
                } else {
                    emitError(error)
                }
            }
        )
    }

    fun getCafeterias() {
        viewModelScope.launch {
            fetchCafeterias()
        }
    }

    private fun getMenusByCafeteria(cafeteriaName: String) {
        viewModelScope.launch {
            todayDietRepository.getTodayDiet(cafeteriaName).handleResponse(
                onSuccess = { menus ->
                    _uiState.update { it.copy(menus = menus) }
                },
                onError = { error ->
                    emitError(error)
                }
            )
        }
    }

    fun toggleMenuLiked(mainMenuId: Long) {
        viewModelScope.launch {
            menuRepository.toggleMenuLiked(mainMenuId = mainMenuId).handleResponse(
                onSuccess = { isLiked ->
                    _uiState.update { currentState ->
                        val updatedMenus = currentState.menus.map { menu ->
                            if (menu.mainMenuId == mainMenuId) {
                                menu.copy(likedMenu = !menu.likedMenu)
                            } else {
                                menu
                            }
                        }
                        currentState.copy(menus = updatedMenus)
                    }
                }
            )
        }
    }

    fun resetState() {
        _uiState.update { MainUiState() }
    }
}