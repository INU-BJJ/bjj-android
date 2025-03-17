package inu.appcenter.bjj_android.repository.item

import inu.appcenter.bjj_android.model.item.ItemResponse
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.model.item.MyPageResponse
import inu.appcenter.bjj_android.repository.base.BaseRepository
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

interface ItemRepository: BaseRepository {
    suspend fun getAllItemsInfo(): Flow<CustomResponse<ItemResponse>>
    suspend fun drawItem(itemType: ItemType): Flow<CustomResponse<ItemResponseItem>>
    suspend fun getItemInfo(itemId: Long): Flow<CustomResponse<ItemResponseItem>>
    suspend fun getMyPageInfo(): Flow<CustomResponse<MyPageResponse>>
    suspend fun wearItem(itemId: Long): Flow<CustomResponse<Unit>>
}