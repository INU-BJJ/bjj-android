package inu.appcenter.bjj_android.ui.mypage

import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.repository.item.ItemRepository
import inu.appcenter.bjj_android.repository.member.MemberRepository
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

    private val _eventFlow = MutableSharedFlow<MyPageUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
                        it.copy(userName = myPageInfo.nickname, point = myPageInfo.point, wearingCharacterId = myPageInfo.itemId, wearingCharacterImageName = myPageInfo.imageName)
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
                    getAllItemsInfo()
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
            viewModelScope.launch {
                _eventFlow.emit(MyPageUiEvent.ShowToast("포인트가 부족합니다."))
            }
            return
        }

        _uiState.update { it.copy(point = currentPoint - itemCost) }

        viewModelScope.launch {
            setLoading(true)
            itemRepository.drawItem(itemType).handleResponse(
                onError = {
                    setLoading(false)
                    _uiState.update { it.copy(point = currentPoint) } // 롤백
                    emitError(it)
                    viewModelScope.launch {
                        _eventFlow.emit(MyPageUiEvent.ShowToast(it.message ?: "오류가 발생했습니다."))
                    }
                },
                onSuccess = { newItem ->
                    setLoading(false)

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
        _uiState.value.drawnItem?.let { wearItem(it.itemId) }
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
    }

}