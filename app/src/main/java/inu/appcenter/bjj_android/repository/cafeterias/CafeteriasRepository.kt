package inu.appcenter.bjj_android.repository.cafeterias

import retrofit2.Response

interface CafeteriasRepository {
    suspend fun getCafeterias() : Response<List<String>>
}