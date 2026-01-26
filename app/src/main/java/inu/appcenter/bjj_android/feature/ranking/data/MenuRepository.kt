package inu.appcenter.bjj_android.feature.ranking.data


import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.core.data.BaseRepository
import inu.appcenter.bjj_android.core.util.CustomResponse
import kotlinx.coroutines.flow.Flow

interface MenuRepository : BaseRepository {
    suspend fun toggleMenuLiked(mainMenuId: Long): Flow<CustomResponse<Boolean>>
    suspend fun getLikedMenus(): Flow<CustomResponse<List<LikedMenu>>>
    suspend fun getMenusRanking(pageNumber: Int, pageSize: Int): Flow<CustomResponse<MenuRanking>>
}