package inu.appcenter.bjj_android.repository.cafeterias

import inu.appcenter.bjj_android.network.APIService
import retrofit2.Response

class CafeteriasRepositoryImpl(private val apiService: APIService) : CafeteriasRepository {
    override suspend fun getCafeterias(): Response<List<String>> {
        return apiService.getCafeterias()
    }
}