package inu.appcenter.bjj_android.feature.profile.data

import inu.appcenter.bjj_android.model.item.ItemResponse
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.model.item.MyPageResponse
import inu.appcenter.bjj_android.core.data.BaseRepository
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

interface ItemRepository: BaseRepository {
    suspend fun getAllItemsInfo(itemType: ItemType): Flow<CustomResponse<ItemResponse>>
    suspend fun drawItem(itemType: ItemType): Flow<CustomResponse<ItemResponseItem>>
    suspend fun getItemInfo(itemType: ItemType,itemId: Long): Flow<CustomResponse<ItemResponseItem>>
    suspend fun getMyPageInfo(): Flow<CustomResponse<MyPageResponse>>
    suspend fun wearItem(itemType: ItemType, itemId: Long): Flow<CustomResponse<Unit>>
}