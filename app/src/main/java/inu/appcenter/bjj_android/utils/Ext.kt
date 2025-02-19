package inu.appcenter.bjj_android.utils

import android.util.Log
import kotlinx.coroutines.flow.Flow

suspend fun <T> Flow<CustomResponse<T>>.collectAndHandle(
    onError: (Throwable?) -> Unit = {
        Log.e("collectAndHandle", "collectAndHandle: $it")
    },
    onLoading: () -> Unit = {},
    stateReducer: (T) -> Unit,
) {
    collect{ response->
        when(response){
            is CustomResponse.Error -> {
                onError(response.error)
            }

            is CustomResponse.Success -> {
                stateReducer(response.data)
            }

            is CustomResponse.Loading -> {
                onLoading()
            }
        }
    }
}