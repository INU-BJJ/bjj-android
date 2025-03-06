package inu.appcenter.bjj_android.ui.component

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import inu.appcenter.bjj_android.utils.ErrorHandler
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ErrorHandler(viewModel: BaseViewModel) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.errorEvent.collectLatest { error ->
            val errorMessage = ErrorHandler.getUserFriendlyMessage(error)
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}