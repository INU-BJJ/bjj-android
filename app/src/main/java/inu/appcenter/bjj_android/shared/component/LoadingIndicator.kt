package inu.appcenter.bjj_android.shared.component


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import inu.appcenter.bjj_android.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoadingIndicator(viewModel: BaseViewModel) {
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        // loadingState로 속성명 변경
        viewModel.loadingState.collectLatest {
            isLoading = it
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFFF7800))
        }
    }
}