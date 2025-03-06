package inu.appcenter.bjj_android.utils

import android.util.Log
import android.widget.ImageView
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
import coil.request.CachePolicy
import coil.request.ImageRequest
import inu.appcenter.bjj_android.R

/**
 * 이미지 로딩 관련 유틸리티 클래스
 * 앱 전체에서 일관된 이미지 로딩 설정과 에러 처리를 제공합니다.
 */
object ImageLoader {
    // 서버 이미지 Base URL
    private const val BASE_URL = "https://bjj.inuappcenter.kr/images/"
    private const val REVIEW_IMAGE_PATH = "review/"
    private const val PROFILE_IMAGE_PATH = "profile/"

    // 다양한 이미지 유형에 대한 URL 생성 헬퍼 함수들
    fun getReviewImageUrl(imageName: String): String {
        return BASE_URL + REVIEW_IMAGE_PATH + imageName
    }

    fun getProfileImageUrl(imageName: String): String {
        return BASE_URL + PROFILE_IMAGE_PATH + imageName
    }

    // 이미지 로딩 요청 빌더
    fun buildImageRequest(
        context: android.content.Context,
        imageName: String,
        imageUrl: String,
        crossfade: Boolean = true,
        enableLogging: Boolean = true
    ): ImageRequest {
        return ImageRequest.Builder(context)
            .data(imageUrl)
            .size(500) // 기본 크기 설정
            .memoryCacheKey(imageName)
            .diskCacheKey(imageName)
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
                            Log.d("ImageLoading", "Successfully loaded image: $imageName")
                        }
                    )
                }
            }
            .build()
    }

    /**
     * Compose용 리뷰 이미지 로딩 컴포저블
     * 로딩 상태와 에러 처리가 통합되어 있습니다.
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
     * 메모리 캐시 정책을 강제로 리프레시하는 함수
     * 프로필 이미지 업데이트 등의 경우에 사용
     */
    fun refreshImageCache(
        context: android.content.Context,
        imageName: String,
        imageUrl: String
    ): ImageRequest {
        return ImageRequest.Builder(context)
            .data(imageUrl)
            .memoryCacheKey(imageName + System.currentTimeMillis()) // 새로운 키를 생성하여 캐시 무시
            .diskCachePolicy(CachePolicy.DISABLED) // 디스크 캐시도 일시적으로 비활성화
            .build()
    }
}