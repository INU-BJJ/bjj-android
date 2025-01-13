package inu.appcenter.bjj_android.repository.menu

import inu.appcenter.bjj_android.model.menu.LikedMenu
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MenuRepository {
    suspend fun toggleMenuLiked(mainMenuId: Long) : Response<Boolean>

    suspend fun getLikedMenus() : Response<LikedMenu>
}