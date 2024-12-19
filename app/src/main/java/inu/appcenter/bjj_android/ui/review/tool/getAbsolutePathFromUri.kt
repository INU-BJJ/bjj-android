package inu.appcenter.bjj_android.ui.review.tool

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun getAbsolutePathFromUri(context: Context, uri: Uri): String? {
    // MediaStore, DocumentProvider 등을 통해 경로를 얻는 일반적인 로직
    // 여기서는 간단한 예시를 보여줍니다.

    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        filePath = cursor.getString(columnIndex)
    }
    return filePath
}
