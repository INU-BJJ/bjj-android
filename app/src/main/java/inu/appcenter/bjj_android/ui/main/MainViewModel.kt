package inu.appcenter.bjj_android.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.model.todaydiet.TodayMenuRes
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val cafeterias: List<String> = emptyList(),
    val selectedCafeteria: String? = null,
    val menus: List<TodayDietRes> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MainViewModel(private val cafeteriasRepository: CafeteriasRepository, private val todayDietRepository: TodayDietRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()


    fun selectCafeteria(cafeteria: String) {
        _uiState.update { it.copy(selectedCafeteria = cafeteria, isLoading = true) }
        getMenusByCafeteria(cafeteria)
    }

    fun getCafeterias(){
        viewModelScope.launch {
            try {
                val response = cafeteriasRepository.getCafeterias()
                if (response.isSuccessful){
                    val cafeterias = response.body() ?: throw Exception("식당 정보가 비어있습니다.")
                    Log.d("getCafeterias",cafeterias.toString() )
                    _uiState.update {
                        it.copy(
                            cafeterias = cafeterias,
                            selectedCafeteria = cafeterias.firstOrNull(),
                            isLoading = false
                        )
                    }
                    // 첫 번째 카페테리아의 메뉴를 자동으로 로드
                    cafeterias.firstOrNull()?.let { firstCafeteria ->
                        getMenusByCafeteria(firstCafeteria)
                    }
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("getCafeterias 실패 원인", e.message.toString())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "카페테리아 정보를 불러오는데 실패했습니다: ${e.message}"
                    )
                }
            }
        }
    }

    fun getMenusByCafeteria(
        cafeteriaName : String
    ){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = todayDietRepository.getTodayDiet(cafeteriaName = cafeteriaName)

                if (response.isSuccessful){
                    val menus = response.body() ?: throw Exception("식당 메뉴 정보가 비어있습니다.")
                    _uiState.update { it.copy(menus = menus, isLoading = false) }
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("getMenusByCafeteria 실패 원인", e.message.toString())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "메뉴를 불러오는데 실패했습니다: ${e.message}"
                    )
                }
            }
        }
    }
}