package inu.appcenter.bjj_android.utils

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import inu.appcenter.bjj_android.R
import okhttp3.OkHttpClient
import okhttp3.Response

/**
 * 이미지 로딩 관련 유틸리티 클래스
 * 앱 전체에서 일관된 이미지 로딩 설정과 에러 처리를 제공합니다.
 */
object ImageLoader {
    // 서버 이미지 Base URL
    private const val BASE_URL = "https://bjj.inuappcenter.kr/images/"
    private const val REVIEW_IMAGE_PATH = "review/"
    private const val PROFILE_IMAGE_PATH = "profile/"
    private const val CHARACTER_IMAGE_PATH = "item/character/"
    private const val BACKGROUND_IMAGE_PATH = "item/background/"
    private const val CAFETERIA_IMAGE_PATH = "cafeteria/"


    // 인증 토큰을 저장할 변수 추가
    private var authToken: String? = null

    // 토큰을 설정하는 함수
    fun setAuthToken(token: String?) {
        authToken = token
    }

    // 인증 헤더를 추가하는 OkHttp 인터셉터
    private class AuthInterceptor : okhttp3.Interceptor {
        override fun intercept(chain: okhttp3.Interceptor.Chain): Response {
            val originalRequest = chain.request()

            // 토큰이 없으면 원래 요청 그대로 진행
            if (authToken.isNullOrEmpty()) {
                return chain.proceed(originalRequest)
            }

            // 토큰이 있으면 Authorization 헤더 추가
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $authToken")
                .build()

            return chain.proceed(newRequest)
        }
    }

    // 커스텀 ImageLoader 생성
    fun createImageLoader(context: android.content.Context): coil.ImageLoader {
        return coil.ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .components {
                // SVG 디코더 추가
                add(coil.decode.SvgDecoder.Factory())
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .build()
            }
            .build()
    }

    // 다양한 이미지 유형에 대한 URL 생성 헬퍼 함수들
    private fun getReviewImageUrl(imageName: String): String {
        return BASE_URL + REVIEW_IMAGE_PATH + imageName
    }

    private fun getProfileImageUrl(imageName: String): String {
        return BASE_URL + PROFILE_IMAGE_PATH + imageName
    }

    private fun getCharacterImageUrl(imageName: String): String {
        return BASE_URL + CHARACTER_IMAGE_PATH + imageName
    }

    private fun getBackgroundImageUrl(imageName: String): String {
        return BASE_URL + BACKGROUND_IMAGE_PATH + imageName
    }

    private fun getCafeteriaImageUrl(imageName: String): String {
        return BASE_URL + CAFETERIA_IMAGE_PATH + imageName
    }

    // 이미지 로딩 요청 빌더
    // 이미지 로딩 요청 빌더
    private fun buildImageRequest(
        context: android.content.Context,
        imageName: String,
        imageUrl: String,
        crossfade: Boolean = true,
        enableLogging: Boolean = true
    ): ImageRequest {
        // URL 기반 캐시 키 생성 - 경로를 포함하여 중복 방지
        val cacheKey = imageUrl.hashCode().toString() + "_" + imageName

        return ImageRequest.Builder(context)
            .data(imageUrl)
            .size(500) // 기본 크기 설정
            .memoryCacheKey(cacheKey) // URL+이미지명 조합의 고유 키 사용
            .diskCacheKey(cacheKey) // 디스크 캐시도 동일한 고유 키 사용
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(crossfade)
            .apply {
                if (enableLogging) {
                    listener(
                        onError = { _, result ->
                            Log.e("ImageLoading", "Error loading image: ${result.throwable.message}", result.throwable)
                        },
                        onSuccess = { _, _ ->
                            Log.d("ImageLoading", "Successfully loaded image: $cacheKey")
                        }
                    )
                }
            }
            .build()
    }

    // 기존 이미지 로딩 함수들은 그대로 유지

    /**
     * Compose용 리뷰 이미지 로딩 컴포저블
     */
    @Composable
    fun ReviewImage(
        imageName: String?,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.Crop,
        shape: Shape? = null,
        showLoading: Boolean = true,
        clickable: Boolean = false,
        isHeaderImage: Boolean = false,
        onClick: (() -> Unit)? = null,
        isLocalImage: Boolean = false,
        localUri: Uri? = null
    ) {
        // 기존 코드 유지
        val context = LocalContext.current

        // 공통 modifier 생성
        val finalModifier = if (shape != null) {
            modifier.clip(shape)
        } else {
            modifier
        }

        // clickable 속성 추가 (필요한 경우)
        val clickableModifier = if (clickable && onClick != null) {
            finalModifier.clickable { onClick() }
        } else {
            finalModifier
        }

        // 로컬 이미지인 경우 (갤러리에서 선택한 이미지)
        if (isLocalImage && localUri != null) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(localUri)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = "로컬 이미지",
                contentScale = contentScale,
                modifier = clickableModifier
            )

            return
        }

        // 서버 이미지 처리 (기존 코드)
        if (imageName == null) {
            // 이미지가 없는 경우 기본 이미지 표시 (동일한 modifier 사용)
            Image(
                painter = painterResource(
                    if (isHeaderImage) R.drawable.big_placeholder else R.drawable.placeholder),
                contentDescription = "기본 이미지",
                contentScale = contentScale,
                modifier = clickableModifier.fillMaxSize() // fillMaxSize 추가하여 공간을 채우도록 함
            )
        } else {
            val imageUrl = getReviewImageUrl(imageName)
            val imageRequest = buildImageRequest(context, imageName, imageUrl)

            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = "리뷰 이미지",
                contentScale = contentScale,
                modifier = clickableModifier,
                loading = {
                    if (showLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.placeholder),
                        contentDescription = "이미지 로딩 실패",
                        contentScale = contentScale,
                        modifier = Modifier.fillMaxSize() // fillMaxSize 추가
                    )
                }
            )
        }
    }

    @Composable
    fun CharacterItem(
        imageName: String?,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.Crop,
        shape: Shape? = null,
        showLoading: Boolean = true,
        clickable: Boolean = false,
        isHeaderImage: Boolean = false,
        onClick: (() -> Unit)? = null,
        isLocalImage: Boolean = false,
        localUri: Uri? = null
    ) {
        val context = LocalContext.current

        // 공통 modifier 생성
        val finalModifier = if (shape != null) {
            modifier.clip(shape)
        } else {
            modifier
        }

        // clickable 속성 추가 (필요한 경우)
        val clickableModifier = if (clickable && onClick != null) {
            finalModifier.clickable { onClick() }
        } else {
            finalModifier
        }

        // 로컬 이미지인 경우 (갤러리에서 선택한 이미지)
        if (isLocalImage && localUri != null) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(localUri)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = "로컬 이미지",
                contentScale = contentScale,
                modifier = clickableModifier
            )

            return
        }

        // 서버 이미지 처리 (기존 코드)
        if (imageName == null) {
            // 이미지가 없는 경우 기본 이미지 표시 (동일한 modifier 사용)
            Image(
                painter = painterResource(
                    if (isHeaderImage) R.drawable.big_placeholder else R.drawable.placeholder),
                contentDescription = "기본 이미지",
                contentScale = contentScale,
                modifier = clickableModifier.fillMaxSize() // fillMaxSize 추가하여 공간을 채우도록 함
            )
        } else {
            val imageUrl = getCharacterImageUrl(imageName)
            val imageRequest = buildImageRequest(context, imageName, imageUrl)

            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = "리뷰 이미지",
                contentScale = contentScale,
                modifier = clickableModifier,
                loading = {
                    if (showLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.placeholder),
                        contentDescription = "이미지 로딩 실패",
                        contentScale = contentScale,
                        modifier = Modifier.fillMaxSize() // fillMaxSize 추가
                    )
                }
            )
        }
    }

    @Composable
    fun BackgroundItem(
        imageName: String?,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.Crop,
        shape: Shape? = null,
        showLoading: Boolean = true,
        clickable: Boolean = false,
        isHeaderImage: Boolean = false,
        onClick: (() -> Unit)? = null,
        isLocalImage: Boolean = false,
        localUri: Uri? = null
    ) {
        val context = LocalContext.current

        // 공통 modifier 생성
        val finalModifier = if (shape != null) {
            modifier.clip(shape)
        } else {
            modifier
        }

        // clickable 속성 추가 (필요한 경우)
        val clickableModifier = if (clickable && onClick != null) {
            finalModifier.clickable { onClick() }
        } else {
            finalModifier
        }

        // 로컬 이미지인 경우 (갤러리에서 선택한 이미지)
        if (isLocalImage && localUri != null) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(localUri)
                    .crossfade(true)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = "로컬 이미지",
                contentScale = contentScale,
                modifier = clickableModifier
            )

            return
        }

        // 서버 이미지 처리 (기존 코드)
        if (imageName == null) {
            // 이미지가 없는 경우 기본 이미지 표시 (동일한 modifier 사용)
            Image(
                painter = painterResource(
                    if (isHeaderImage) R.drawable.big_placeholder else R.drawable.placeholder),
                contentDescription = "기본 이미지",
                contentScale = contentScale,
                modifier = clickableModifier.fillMaxSize() // fillMaxSize 추가하여 공간을 채우도록 함
            )
        } else {
            val imageUrl = getBackgroundImageUrl(imageName)
            val imageRequest = buildImageRequest(context, imageName, imageUrl)
            Log.d("imageUrl", imageUrl.toString())
            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = "리뷰 이미지",
                contentScale = contentScale,
                modifier = clickableModifier,
                loading = {
                    if (showLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.placeholder),
                        contentDescription = "이미지 로딩 실패",
                        contentScale = contentScale,
                        modifier = Modifier.fillMaxSize() // fillMaxSize 추가
                    )
                }
            )
        }
    }

    /**
     * Compose용 프로필 이미지 로딩 컴포저블
     */
    @Composable
    fun ProfileImage(
        imageName: String?,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.Crop,
        shape: Shape? = null
    ) {
        val context = LocalContext.current
        if (imageName == null) {
            // 기본 프로필 이미지 표시
            Image(
                painter = painterResource(R.drawable.mypage), // 기본 프로필 이미지 리소스
                contentDescription = "기본 프로필 이미지",
                contentScale = contentScale,
                modifier = if (shape != null) modifier.clip(shape) else modifier
            )
        } else {
            val imageUrl = getProfileImageUrl(imageName)
            val imageRequest = buildImageRequest(context, imageName, imageUrl)

            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = "프로필 이미지",
                contentScale = contentScale,
                modifier = if (shape != null) modifier.clip(shape) else modifier,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 1.5.dp
                        )
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.mypage), // 기본 프로필 이미지 리소스
                        contentDescription = "프로필 이미지 로딩 실패",
                        contentScale = contentScale
                    )
                }
            )
        }
    }

    /**
     * Compose용 지도 이미지 로딩 컴포저블
     */
    @Composable
    fun MapImage(
        imageName: String?,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.Crop,
        shape: Shape? = null,
        showLoading: Boolean = true,
        clickable: Boolean = false,
        onClick: (() -> Unit)? = null
    ) {
        val context = LocalContext.current

        // 공통 modifier 생성
        val finalModifier = if (shape != null) {
            modifier.clip(shape)
        } else {
            modifier
        }

        // clickable 속성 추가 (필요한 경우)
        val clickableModifier = if (clickable && onClick != null) {
            finalModifier.clickable { onClick() }
        } else {
            finalModifier
        }

        // 서버 이미지 처리
        if (imageName == null) {
            // 이미지가 없는 경우 기본 지도 이미지 표시
            Image(
                painter = painterResource(R.drawable.big_placeholder), // 기본 지도 이미지 리소스 (추가 필요)
                contentDescription = "기본 지도 이미지",
                contentScale = contentScale,
                modifier = clickableModifier.fillMaxSize()
            )
        } else {
            val imageUrl = getCafeteriaImageUrl(imageName)
            val imageRequest = buildImageRequest(context, imageName, imageUrl)

            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = "지도 이미지",
                contentScale = contentScale,
                modifier = clickableModifier,
                loading = {
                    if (showLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.big_placeholder), // 기본 지도 이미지 리소스 (추가 필요)
                        contentDescription = "지도 이미지 로딩 실패",
                        contentScale = contentScale,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )
        }
    }

    /**
     * 메모리 캐시 정책을 강제로 리프레시하는 함수
     * 프로필 이미지 업데이트 등의 경우에 사용
     */
    fun refreshImageCache(
        context: android.content.Context,
        imageName: String,
        imageUrl: String
    ): ImageRequest {
        val cacheKey = imageUrl.hashCode().toString() + "_" + imageName + "_" + System.currentTimeMillis()

        return ImageRequest.Builder(context)
            .data(imageUrl)
            .memoryCacheKey(cacheKey) // 고유 캐시 키 + 타임스탬프
            .diskCachePolicy(CachePolicy.DISABLED) // 디스크 캐시도 일시적으로 비활성화
            .build()
    }
}