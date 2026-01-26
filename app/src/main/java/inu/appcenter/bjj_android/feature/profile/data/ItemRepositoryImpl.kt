package inu.appcenter.bjj_android.feature.profile.data

import inu.appcenter.bjj_android.model.item.ItemResponse
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.model.item.MyPageResponse
import inu.appcenter.bjj_android.core.data.remote.APIService
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

class ItemRepositoryImpl(private val apiService: APIService): ItemRepository  {
    override suspend fun getAllItemsInfo(itemType: ItemType): Flow<CustomResponse<ItemResponse>> = safeApiCall {
        apiService.getAllItemsInfo(itemType = itemType)
    }

    override suspend fun drawItem(itemType: ItemType): Flow<CustomResponse<ItemResponseItem>> = safeApiCall {
        apiService.drawItem(itemType = itemType)
    }

    override suspend fun getItemInfo(itemType: ItemType, itemId: Long): Flow<CustomResponse<ItemResponseItem>> = safeApiCall {
        apiService.getItemInfo(itemType = itemType, itemIdx = itemId)
    }

    override suspend fun getMyPageInfo(): Flow<CustomResponse<MyPageResponse>> = safeApiCall {
        apiService.getMyPageInfo()
    }

    override suspend fun wearItem(itemType: ItemType, itemId: Long): Flow<CustomResponse<Unit>> = safeApiCall {
        apiService.wearItem(itemType = itemType, itemIdx = itemId)
    }

}