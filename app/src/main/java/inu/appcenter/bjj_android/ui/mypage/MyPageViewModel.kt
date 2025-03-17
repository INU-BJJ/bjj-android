package inu.appcenter.bjj_android.ui.mypage

import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.repository.item.ItemRepository
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailUiEvent
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



sealed class MyPageUiEvent {
    data class ShowToast(val message: String) : MyPageUiEvent()
}

data class MyPageUiState(
    val userName: String = "",
    val wearingBackgroundImageName: String? = null,
    val wearingCharacterImageName: String? = null,
    val wearingBackgroundId: Long? = null,
    val wearingCharacterId: Long? = null,
    val point: Int = 0,
    val items: List<ItemResponseItem> = emptyList(),
)

class MyPageViewModel(private val itemRepository: ItemRepository) : BaseViewModel() {

    companion object {
        private const val CHARACTER_COST = 100
        private const val BACKGROUND_COST = 150
    }

    private val _eventFlow = MutableSharedFlow<MyPageUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState = _uiState.asStateFlow()

    fun getMyPageInfo() {
        viewModelScope.launch {
            setLoading(true)
            itemRepository.getMyPageInfo().handleResponse(
                onLoading = {

                },
                onError = {
                    setLoading(false)
                    emitError(it)
                    viewModelScope.launch {
                        _eventFlow.emit(
                            MyPageUiEvent.ShowToast(
                                it.message ?: "오류가 발생했습니다."
                            )
                        )
                    }
                },
                onSuccess = { myPageInfo ->
                    setLoading(false)
                    _uiState.update {
                        it.copy(userName = myPageInfo.nickname, point = myPageInfo.point)
                    }
                }
            )
        }
    }

    fun getAllItemsInfo(){
        viewModelScope.launch {
            setLoading(true)
            itemRepository.getAllItemsInfo().handleResponse(
                onLoading = {

                },
                onError = {
                    setLoading(false)
                    emitError(it)
                    viewModelScope.launch {
                        _eventFlow.emit(MyPageUiEvent.ShowToast(it.message ?: "오류가 발생했습니다."))
                    }
                },
                onSuccess = { items ->
                    setLoading(false)
                    _uiState.update {
                        it.copy(items = items)
                    }
                }
            )
        }
    }

    fun wearItem(itemId: Long) {

        val itemToWear = _uiState.value.items.find { it.itemId == itemId } ?: return

        viewModelScope.launch {
            setLoading(true)
            updateWearingItemLocally(itemToWear)

            itemRepository.wearItem(itemId).handleResponse(
                onLoading = {

                },
                onError = {
                    setLoading(false)
                    emitError(it)
                    viewModelScope.launch {
                        _eventFlow.emit(MyPageUiEvent.ShowToast(it.message ?: "오류가 발생했습니다."))
                    }

                    getMyPageInfo()
                },
                onSuccess = {
                    setLoading(false)
                    viewModelScope.launch {
                        _eventFlow.emit(MyPageUiEvent.ShowToast("아이템이 장착되었습니다."))
                    }
                }
            )
        }
    }

    // 로컬 UI 업데이트를 위한 도우미 함수
    private fun updateWearingItemLocally(item: ItemResponseItem) {
        when (item.itemType.uppercase()) {
            "CHARACTER" -> _uiState.update {
                it.copy(
                    wearingCharacterImageName = item.imageName,
                    wearingCharacterId = item.itemId
                )
            }
            "BACKGROUND" -> _uiState.update {
                it.copy(
                    wearingBackgroundImageName = item.imageName,
                    wearingBackgroundId = item.itemId
                )
            }
        }
    }

    fun drawItem(itemType: ItemType) {
        // 뽑기 전 포인트 저장 (롤백을 위해)
        val currentPoint = _uiState.value.point
        // 아이템 가격 (실제 서비스의 가격 정책에 맞게 조정 필요)
        val itemCost = getItemCost(itemType)

        // 포인트가 부족한 경우 체크
        if (currentPoint < itemCost) {
            viewModelScope.launch {
                _eventFlow.emit(MyPageUiEvent.ShowToast("포인트가 부족합니다."))
            }
            return
        }

        _uiState.update { it.copy(point = currentPoint - itemCost) }

        viewModelScope.launch {
            setLoading(true)
            itemRepository.drawItem(itemType).handleResponse(
                onLoading = {

                },
                onError = {
                    setLoading(false)

                    // 에러 발생 시 원래 포인트로 롤백
                    _uiState.update { it.copy(point = currentPoint) }

                    emitError(it)
                    viewModelScope.launch {
                        _eventFlow.emit(MyPageUiEvent.ShowToast(it.message ?: "오류가 발생했습니다."))
                    }
                },
                onSuccess = { newItem ->
                    setLoading(false)

                    val updatedItems = _uiState.value.items.map { existingItem ->
                        if (existingItem.itemId == newItem.itemId) {
                            // 이미 목록에 있는 아이템이라면 소유 상태 등을 업데이트
                            existingItem.copy(isOwned = true) // 실제 모델에 맞게 조정 필요
                        } else {
                            existingItem
                        }
                    }

                    _uiState.update { it.copy(items = updatedItems) }

                    // 아이템 획득 후 전체 아이템 목록과 마이페이지 정보를 새로 가져옴
                    getAllItemsInfo()
                    getMyPageInfo()
                }
            )
        }
    }

    private fun getItemCost(itemType: ItemType): Int {
        return when (itemType) {
            ItemType.CHARACTER -> CHARACTER_COST
            ItemType.BACKGROUND -> BACKGROUND_COST
        }
    }

}