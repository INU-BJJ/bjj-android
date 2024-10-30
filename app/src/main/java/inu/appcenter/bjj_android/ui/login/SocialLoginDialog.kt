package inu.appcenter.bjj_android.ui.login

import android.net.Uri
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun SocialLoginDialog(
    onLoginSuccessAlreadySignup : () -> Unit,
    onLoginSuccessFirst : () -> Unit,
    onLoginFailure: () -> Unit,
    authViewModel: AuthViewModel,
    socialLogin: String
) {
    val saveTokenState by authViewModel.saveTokenState.collectAsState()

    LaunchedEffect(saveTokenState) {
        when (saveTokenState) {
            true -> {
                onLoginSuccessAlreadySignup()
                authViewModel.resetSaveTokenState()
            }
            false -> {
                onLoginFailure()
                authViewModel.resetSaveTokenState()
            }
            null -> {} // 초기 상태나 리셋 상태
        }
    }

    Dialog(
        onDismissRequest = { onLoginFailure() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        if (socialLogin.lowercase() == "google") {
                            userAgentString = "Mozilla/5.0 AppleWebKit/535.19 Chrome/56.0.0 Mobile Safari/535.19"
                        }
                        if (socialLogin.lowercase() == "kakao") {
                            layoutParams = android.view.ViewGroup.LayoutParams(
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }

                    }
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            url?.let {
                                //이미 회원가입은 했고 로그인을 시도할 때
                                if (it.startsWith("https://bjj.inuappcenter.kr/api/member/success")) {
                                    val uri = Uri.parse(it)
                                    val token = uri.getQueryParameter("token")
                                    token?.let { token ->
                                        authViewModel.saveToken(token)
                                    }
                                    onLoginSuccessAlreadySignup()
                                    return true
                                }
                                //새로운 회원가입에 성공했을 때
                                if (it.startsWith("https://bjj.inuappcenter.kr/api/member/sign-up")) {
                                    // URL에서 이메일 추출
                                    val uri = Uri.parse(it)
                                    val email = uri.getQueryParameter("email")
                                    email?.let { extractedEmail ->
                                        authViewModel.setSignupEmail(extractedEmail)
                                    }
                                    onLoginSuccessFirst()
                                    return true
                                }
                            }
                            return false
                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            Log.d("WebView", "Page started loading: $url")
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            Log.d("WebView", "Page finished loading: $url")
                        }

                        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                            super.onReceivedError(view, request, error)
                            Log.e("WebView", "Error: ${error?.description}, Code: ${error?.errorCode}")
                            onLoginFailure()
                        }
                    }
                    loadUrl("https://bjj.inuappcenter.kr/oauth2/authorization/${socialLogin}")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}