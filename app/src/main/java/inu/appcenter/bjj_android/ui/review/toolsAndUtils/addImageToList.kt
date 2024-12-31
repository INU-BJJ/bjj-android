package inu.appcenter.bjj_android.ui.review.toolsAndUtils

import android.net.Uri


fun addImageToList(
    uri: Uri,
    photos: MutableList<Uri?>,
    setCurrentCounting: (Int) -> Unit
) {
    val lastNullIndex = photos.indexOfLast { it == null }
    if (lastNullIndex != -1) {
        photos[lastNullIndex] = uri
        val currentCount = photos.count { it != null }
        setCurrentCounting(currentCount)

        if (currentCount < 4 && photos.lastOrNull() != null) {
            // 마지막이 이미지라면 다음 추가 가능하도록 null 박스 추가
            photos.add(null)
        }
    }
}