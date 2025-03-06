package inu.appcenter.bjj_android.ui.mypage.setting.likedmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LikedMenuViewModel(
    private val menuRepository: MenuRepository
) : ViewModel() {
    data class LikedMenuState(
        val likedMenus: List<LikedMenu> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val notificationEnabled: Boolean = true
    )
    private val _uiState = MutableStateFlow(LikedMenuState())
    val uiState: StateFlow<LikedMenuState> = _uiState.asStateFlow()

    init {
        getLikedMenus()
    }

    // 좋아요한 메뉴 목록 불러오기
    fun getLikedMenus() {
        viewModelScope.launch {
            try {
                // 로딩 상태 설정
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                // 리포지토리에서 데이터 불러오기
                val response = menuRepository.getLikedMenus()

                if (response.isSuccessful && response.body() != null) {
                    // 응답이 성공적일 경우 UI 상태 업데이트
                    val likedMenus = response.body() ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        likedMenus = likedMenus,
                        isLoading = false
                    )
                } else {
                    // 응답이 실패한 경우 에러 상태 설정
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "데이터를 불러올 수 없습니다."
                    )
                }
            } catch (e: Exception) {
                // 예외 발생 시 에러 상태 설정
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "알 수 없는 오류가 발생했습니다."
                )
            }
        }
    }

    // 메뉴 좋아요 토글
    fun toggleLike(menuId: Long) {
        viewModelScope.launch {
            try {
                val response = menuRepository.toggleMenuLiked(menuId)
                if (response.isSuccessful) {
                    // 좋아요 토글 성공시 목록 다시 불러오기
                    getLikedMenus()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "좋아요 상태를 변경하는 중 오류가 발생했습니다."
                )
            }
        }
    }

    // 알림 설정 업데이트
    fun toggleNotification(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationEnabled = enabled)
        // TODO: 알림 설정 저장 로직 구현 (Repository 또는 DataStore 활용)
    }
}