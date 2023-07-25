package com.project.gallery.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun Any.io(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO).launch(block = block)
}

fun Any.default(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Default).launch(block = block)
}

fun Any.main(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch(block = block)
}

fun Uri.loadThumbnail(contentResolver: ContentResolver, id: Long): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentResolver.loadThumbnail(
            this,
            Size(100, 100),
            CancellationSignal()
        )
    } else {
        MediaStore.Video.Thumbnails.getThumbnail(
            contentResolver, id,
            MediaStore.Images.Thumbnails.MINI_KIND, null
        )
    }
}