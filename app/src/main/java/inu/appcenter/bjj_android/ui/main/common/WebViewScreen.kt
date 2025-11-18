package inu.appcenter.bjj_android.ui.main.common

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.ui.theme.White_FFFFFF

// JavaScript Interface for communication between WebView and Android
class WebAppInterface(private val context: Context) {
    @JavascriptInterface
    fun showWelcomePointResult(success: Boolean) {
        Log.d("WebAppInterface", "showWelcomePointResult called with success=$success")

        // UI 스레드에서 다이얼로그 표시
        (context as? android.app.Activity)?.runOnUiThread {
            val imageResId = if (success) {
                R.drawable.welcome_success
            } else {
                R.drawable.welcome_fail
            }

            // 커스텀 다이얼로그 생성
            val dialog = Dialog(context)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                // 시스템 UI 유지 (하단바 안 올라오게)
                decorView.systemUiVisibility = (
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val imageView = ImageView(context).apply {
                setImageResource(imageResId)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                // 이미지 클릭 시에도 닫히도록
                setOnClickListener {
                    dialog.dismiss()
                }
            }

            dialog.setContentView(imageView)
            // 배경 클릭 시 닫힘
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    url: String,
    navController: NavController
) {
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)
    val decodedUrl = Uri.decode(url)

    // 토큰을 상태로 수집
    val token by dataStoreManager.token.collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "이벤트",
                        style = LocalTypography.current.bold18.copy(
                            lineHeight = 23.sp,
                            color = Color(0xFF000000),
                            letterSpacing = 0.13.sp,
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = "뒤로가기",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = White_FFFFFF
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        // JavaScript Interface 추가
                        addJavascriptInterface(WebAppInterface(context), "Android")

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                // 페이지 로드 후 토큰 주입 및 버튼 클릭 이벤트 가로채기
                                if (!token.isNullOrEmpty()) {
                                    val script = """
                                        (function() {
                                            // 토큰 주입
                                            window.accessToken = 'Bearer $token';
                                            console.log('Token injected');

                                            // 버튼 클릭 이벤트 가로채기
                                            const claimButton = document.getElementById('claimButton');
                                            if (claimButton) {
                                                console.log('Claim button found, adding listener');
                                                claimButton.addEventListener('click', function(e) {
                                                    e.preventDefault();
                                                    e.stopPropagation();
                                                    console.log('Button clicked, calling API');

                                                    const token = window.accessToken || '';
                                                    fetch('/api/events/welcome-point', {
                                                        method: 'POST',
                                                        headers: {
                                                            'Authorization': token,
                                                            'Content-Type': 'application/json'
                                                        }
                                                    })
                                                    .then(res => {
                                                        console.log('Response status:', res.status);
                                                        if (!res.ok) throw new Error('Server error');
                                                        return res.json();
                                                    })
                                                    .then(data => {
                                                        console.log('Response data:', JSON.stringify(data));
                                                        // 안드로이드 네이티브 다이얼로그 호출
                                                        Android.showWelcomePointResult(data.participationCompleted);
                                                    })
                                                    .catch(err => {
                                                        console.error('Error:', err);
                                                        Android.showWelcomePointResult(false);
                                                    });
                                                }, true);
                                            } else {
                                                console.log('Claim button NOT found');
                                            }
                                        })();
                                    """.trimIndent()
                                    view?.evaluateJavascript(script, null)
                                }
                            }
                        }

                        // WebChromeClient 설정하여 JavaScript 콘솔 로그 캡처
                        webChromeClient = object : WebChromeClient() {
                            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                                Log.d("WebViewConsole", "${consoleMessage.messageLevel()}: ${consoleMessage.message()} -- From line ${consoleMessage.lineNumber()} of ${consoleMessage.sourceId()}")
                                return true
                            }
                        }

                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false
                        settings.domStorageEnabled = true
                        settings.databaseEnabled = true
                        settings.loadsImagesAutomatically = true
                        settings.blockNetworkImage = false
                    }
                },
                update = { webView ->
                    // 토큰이 있으면 헤더에 포함해서 요청
                    if (!token.isNullOrEmpty()) {
                        val headers = mutableMapOf<String, String>()
                        headers["Authorization"] = "Bearer $token"
                        // 필요한 경우 다른 헤더도 추가할 수 있습니다
                        // headers["Content-Type"] = "application/json"

                        webView.loadUrl(decodedUrl, headers)
                    } else {
                        // 토큰이 없으면 일반적인 방식으로 로드
                        webView.loadUrl(decodedUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}