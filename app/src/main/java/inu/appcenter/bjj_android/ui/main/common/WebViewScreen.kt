package inu.appcenter.bjj_android.ui.main.common

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.ui.theme.White_FFFFFF

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
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false
                        settings.domStorageEnabled = true // DOM Storage 활성화
                        settings.databaseEnabled = true // Database 활성화
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