package inu.appcenter.bjj_android.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.menu.MenuRankingDetail
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import inu.appcenter.bjj_android.utils.collectAndHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RankingUiState(
    val rankingList: List<MenuRankingDetail> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastUpdatedAt: String? = null
)

class RankingViewModel(
    private val menuRepository: MenuRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false

    init {
        getMenuRankingList()
    }

    fun getMenuRankingList() = viewModelScope.launch {
        if (isLastPage || _uiState.value.isLoading) return@launch

        menuRepository.getMenusRanking(currentPage, 10).collectAndHandle(
            onError = { error ->
                _uiState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _uiState.update {
                    it.copy(isLoading = true, error = null)
                }
            },
            stateReducer = { menuRanking ->
                isLastPage = menuRanking.lastPage
                currentPage++

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = null,
                        rankingList = currentState.rankingList + menuRanking.menuRankingDetailList,
                        // 첫 페이지일 경우에만 lastUpdatedAt 업데이트
                        lastUpdatedAt = if(currentPage == 1)
                            menuRanking.menuRankingDetailList.firstOrNull()?.updatedAt
                        else
                            currentState.lastUpdatedAt
                    )
                }
            }
        )
    }
}