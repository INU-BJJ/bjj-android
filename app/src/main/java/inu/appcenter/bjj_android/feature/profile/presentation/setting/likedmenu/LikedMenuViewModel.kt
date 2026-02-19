package inu.appcenter.bjj_android.feature.profile.presentation.setting.likedmenu

import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.core.data.local.DataStoreManager
import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.feature.menudetail.data.MenuRepository
import inu.appcenter.bjj_android.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UiEvent sealed class
sealed class LikedMenuUiEvent {
    data class ShowToast(val message: String) : LikedMenuUiEvent()
}

data class LikedMenuUiState(
    val likedMenus: List<LikedMenu> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val notificationEnabled: Boolean = true
)

class LikedMenuViewModel(
    private val menuRepository: MenuRepository,
    private val dataStoreManager: DataStoreManager
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<LikedMenuUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(LikedMenuUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLikedMenus()
        getNotificationSettings()
    }

    // 좋아요한 메뉴 목록 불러오기
    fun getLikedMenus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            menuRepository.getLikedMenus().handleResponse(
                onSuccess = { likedMenus ->
                    _uiState.update {
                        it.copy(
                            likedMenus = likedMenus,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onError = { error ->
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(LikedMenuUiEvent.ShowToast(
                            error.message ?: "메뉴 목록을 불러오는 중 오류가 발생했습니다."
                        ))
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "알 수 없는 오류가 발생했습니다."
                        )
                    }
                }
            )
        }
    }

    // 메뉴 좋아요 토글
    fun toggleLike(menuId: Long) {
        viewModelScope.launch {
            menuRepository.toggleMenuLiked(menuId).handleResponse(
                onSuccess = { isLiked ->
                    getLikedMenus()
                },
                onError = { error ->
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(LikedMenuUiEvent.ShowToast(
                            error.message ?: "좋아요 상태를 변경하는 중 오류가 발생했습니다."
                        ))
                    }
                }
            )
        }
    }

    // 알림 설정 불러오기
    private fun getNotificationSettings() {
        viewModelScope.launch {
            dataStoreManager.getLikedMenuNotification.collect { enabled ->
                _uiState.update {
                    it.copy(notificationEnabled = enabled)
                }
            }
        }
    }

    // 알림 설정 업데이트
    fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveLikedMenuNotification(enabled)
            _uiState.update {
                it.copy(notificationEnabled = enabled)
            }
        }
    }

    // 상태 초기화
    fun resetState() {
        _uiState.update { LikedMenuUiState() }
    }
}