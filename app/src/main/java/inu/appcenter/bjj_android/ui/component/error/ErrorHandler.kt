package inu.appcenter.bjj_android.ui.component.error

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.ui.component.error.handlers.AuthErrorHandler
import inu.appcenter.bjj_android.ui.component.error.handlers.DialogErrorHandler
import inu.appcenter.bjj_android.ui.component.error.handlers.ToastErrorHandler
import inu.appcenter.bjj_android.viewmodel.BaseViewModel

/**
 * 통합 에러 처리 컴포넌트
 * 모든 에러 처리 로직을 내부적으로 분리하되 단일 API로 제공
 *
 * @param viewModel 에러 이벤트를 발생시키는 BaseViewModel
 * @param navController 화면 이동을 위한 NavHostController (인증 만료 처리용)
 * @param showErrorDialog 다이얼로그 표시 여부 (기본값: true)
 */
@Composable
fun ErrorHandler(
    viewModel: BaseViewModel,
    navController: NavHostController? = null,
    showErrorDialog: Boolean = true
) {
    // 토스트 메시지 처리
    ToastErrorHandler(viewModel)

    // 인증 만료 처리
    AuthErrorHandler(viewModel, navController)

    // 에러 다이얼로그 처리 (옵션)
    if (showErrorDialog) {
        DialogErrorHandler(viewModel)
    }
}