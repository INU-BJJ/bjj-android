package inu.appcenter.bjj_android.feature.auth.presentation.login

import android.net.Uri
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SocialLoginDialog(
    onLoginSuccessAlreadySignup : () -> Unit,
    onLoginSuccessFirst : () -> Unit,
    onLoginFailure: () -> Unit,
    authViewModel: AuthViewModel,
    authUiState: AuthUiState
) {
    Log.d("SocialLoginDialog", "=== Dialog Composed/Recomposed ===")

    val lifecycleOwner = LocalLifecycleOwner.current

    // WebView 이벤트 수집 (생명주기 안전)
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            authViewModel.webViewEvents.collectLatest { event ->
                when (event) {
                    is WebViewEvent.PageStarted -> {
                        Log.d("SocialLoginDialog", "Page started: ${event.url}")
                    }
                    is WebViewEvent.PageFinished -> {
                        Log.d("SocialLoginDialog", "Page finished: ${event.url}")
                    }
                    is WebViewEvent.LoginSuccess -> {
                        Log.d("SocialLoginDialog", "✓ Login success with token")
                        onLoginSuccessAlreadySignup()
                        authViewModel.resetState()
                    }
                    is WebViewEvent.SignupSuccess -> {
                        Log.d("SocialLoginDialog", "✓ Signup success with email: ${event.email}")
                        onLoginSuccessFirst()
                    }
                    is WebViewEvent.Error -> {
                        Log.e("SocialLoginDialog", "WebView error: ${event.message}")
                        onLoginFailure()
                    }
                    is WebViewEvent.RenderProcessGone -> {
                        Log.e("SocialLoginDialog", "!!! Render process gone - closing dialog !!!")
                        onLoginFailure()
                    }
                }
            }
        }
    }

    LaunchedEffect(authUiState.saveTokenState) {
        when (authUiState.saveTokenState) {
            true -> {
                Log.d("SocialLoginDialog", "Token saved successfully")
            }
            false -> {
                Log.d("SocialLoginDialog", "Token save failed")
                onLoginFailure()
                authViewModel.resetState()
            }
            null -> {
                Log.d("SocialLoginDialog", "saveTokenState is null (initial/reset)")
            }
        }
    }

    Dialog(
        onDismissRequest = { onLoginFailure() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AndroidView(
            factory = { context ->
                Log.d("SocialLoginDialog", "=== WebView Factory Called ===")
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        if (authUiState.socialName.lowercase() == "google") {
                            userAgentString = "Mozilla/5.0 AppleWebKit/535.19 Chrome/56.0.0 Mobile Safari/535.19"
                        }
                        if (authUiState.socialName.lowercase() == "kakao") {
                            layoutParams = android.view.ViewGroup.LayoutParams(
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }

                    }
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            url?.let { authViewModel.onWebViewPageStarted(it) }
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            url?.let { authViewModel.onWebViewPageFinished(it) }
                        }

                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            Log.d("SocialLoginDialog", "shouldOverrideUrlLoading: $url")
                            url?.let {
                                //이미 회원가입은 했고 로그인을 시도할 때
                                if (it.startsWith("https://bjj.inuappcenter.kr/api/members/success")) {
                                    val uri = Uri.parse(it)
                                    val token = uri.getQueryParameter("token")
                                    token?.let { token ->
                                        authViewModel.onWebViewLoginSuccess(token)
                                    }
                                    return true
                                }
                                //새로운 회원가입에 성공했을 때
                                if (it.startsWith("https://bjj.inuappcenter.kr/api/members/sign-up")) {
                                    val uri = Uri.parse(it)
                                    val email = uri.getQueryParameter("email")
                                    email?.let { extractedEmail ->
                                        authViewModel.onWebViewSignupSuccess(extractedEmail)
                                    }
                                    return true
                                }
                            }
                            return false
                        }

                        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                            super.onReceivedError(view, request, error)
                            Log.e("SocialLoginDialog", "WebView error: ${error?.description}, errorCode: ${error?.errorCode}")

                            // WebView 렌더 프로세스 종료로 인한 에러는 무시
                            // (onRenderProcessGone에서 처리됨)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                if (error?.errorCode == android.webkit.WebViewClient.ERROR_UNKNOWN) {
                                    Log.e("SocialLoginDialog", "Ignoring ERROR_UNKNOWN (likely render process gone)")
                                    return
                                }
                            }

                            authViewModel.onWebViewError(error?.description?.toString() ?: "Unknown error")
                        }

                        override fun onRenderProcessGone(view: WebView?, detail: android.webkit.RenderProcessGoneDetail?): Boolean {
                            Log.e("SocialLoginDialog", "!!! WebView Render Process Gone !!!")
                            Log.e("SocialLoginDialog", "Did crash: ${detail?.didCrash()}")

                            // ViewModel을 통해 이벤트 발생
                            authViewModel.onWebViewRenderProcessGone()

                            // true를 반환하여 앱 크래시 방지
                            return true
                        }
                    }
                    val initialUrl = "https://bjj.inuappcenter.kr/oauth2/authorization/${authUiState.socialName}"
                    Log.d("SocialLoginDialog", "Loading initial URL: $initialUrl")
                    loadUrl(initialUrl)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    // WebView 정리
    DisposableEffect(Unit) {
        onDispose {
            Log.d("SocialLoginDialog", "Dialog disposed - cleaning up")
        }
    }
}
