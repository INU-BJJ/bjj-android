package inu.appcenter.bjj_android.ui.mypage

import android.util.Log
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.repository.item.ItemRepository
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
    val selectedCategory: ItemType = ItemType.CHARACTER,
    val point: Long = 0,
    val items: List<ItemResponseItem> = emptyList(),
    val isDrawSuccess: Boolean = false,
    val drawnItem: ItemResponseItem? = null,
)

class MyPageViewModel(private val itemRepository: ItemRepository) : BaseViewModel() {

    companion object {
        private const val CHARACTER_COST = 50
        private const val BACKGROUND_COST = 100
    }

    // MyPageUiEvent를 사용하지 않고 BaseViewModel의 toastEvent 사용
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMyPageInfo()
        getAllItemsInfo()
    }

    fun getMyPageInfo() {
        viewModelScope.launch {
            setLoading(true)
            itemRepository.getMyPageInfo().handleResponse(
                onSuccess = { myPageInfo ->
                    _uiState.update {
                        it.copy(
                            userName = myPageInfo.nickname,
                            point = myPageInfo.point,
                            wearingCharacterId = myPageInfo.characterId,
                            wearingCharacterImageName = myPageInfo.characterImageName,
                            wearingBackgroundId = myPageInfo.backgroundId,
                            wearingBackgroundImageName = myPageInfo.backgroundImageName
                        )
                    }
                }
                // onError는 기본 처리 사용
            )
        }
    }

    fun getAllItemsInfo() {
        viewModelScope.launch {
            setLoading(true)
            itemRepository.getAllItemsInfo(uiState.value.selectedCategory).handleResponse(
                onSuccess = { items ->
                    _uiState.update {
                        it.copy(items = items)
                    }
                }
                // onError는 기본 처리 사용
            )
        }
    }

    fun wearItem(item: ItemResponseItem) {
        val itemToWear = _uiState.value.items.find { it.itemId == item.itemId } ?: return

        viewModelScope.launch {
            updateWearingItemLocally(itemToWear)

            itemRepository.wearItem(itemType = item.itemType.toItemType(), itemId = item.itemId).handleResponse(
                onSuccess = {
                    Log.d("WearItemSuccess", "아이템 장착 성공")
                    showToast("아이템이 장착되었습니다.")
                    getAllItemsInfo()
                },
                onError = { error ->
                    Log.d("WearItemError", error.message.toString())
                    getMyPageInfo()  // 장착 실패 시 원래 상태로 복원
                    // 기본 오류 처리 외에도 상태 복원 로직 추가
                    handleError(error)
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
        val currentPoint = _uiState.value.point
        val itemCost = getItemCost(itemType)

        if (currentPoint < itemCost) {
            showToast("포인트가 부족합니다.")
            return
        }

        _uiState.update { it.copy(point = currentPoint - itemCost) }

        viewModelScope.launch {
            itemRepository.drawItem(itemType).handleResponse(
                onSuccess = { newItem ->
                    // 뽑은 아이템 성공 다이얼로그 상태 업데이트
                    _uiState.update {
                        it.copy(
                            isDrawSuccess = true,
                            drawnItem = newItem,
                        )
                    }
                    // 아이템 목록 갱신
                    getAllItemsInfo()
                    getMyPageInfo()
                },
                onError = { error ->
                    _uiState.update { it.copy(point = currentPoint) } // 롤백
                    handleError(error)
                }
            )
        }
    }

    fun resetDrawState() {
        _uiState.update {
            it.copy(
                isDrawSuccess = false,
                drawnItem = null,
            )
        }
    }

    fun equipDrawnItem() {
        _uiState.value.drawnItem?.let { wearItem(it) }
    }

    private fun getItemCost(itemType: ItemType): Int {
        return when (itemType) {
            ItemType.CHARACTER -> CHARACTER_COST
            ItemType.BACKGROUND -> BACKGROUND_COST
        }
    }

    fun selectCategory(itemType: ItemType) {
        _uiState.update {
            it.copy(selectedCategory = itemType)
        }
        getAllItemsInfo()
    }
}

fun String.toItemType(): ItemType {
    return when (this.uppercase()) {
        "CHARACTER" -> ItemType.CHARACTER
        "BACKGROUND" -> ItemType.BACKGROUND
        else -> throw IllegalArgumentException("Unknown item type: $this")
    }
}