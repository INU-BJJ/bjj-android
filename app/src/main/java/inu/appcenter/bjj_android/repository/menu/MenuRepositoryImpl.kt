package inu.appcenter.bjj_android.repository.menu

import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.network.APIService
import retrofit2.Response

class MenuRepositoryImpl(private val apiService: APIService) : MenuRepository {
    override suspend fun toggleMenuLiked(mainMenuId: Long): Response<Boolean> {
        return apiService.toggleMenuLiked(mainMenuId = mainMenuId)
    }

    override suspend fun getLikedMenus(): Response<LikedMenu> {
        return apiService.getLikedMenus()
    }
}