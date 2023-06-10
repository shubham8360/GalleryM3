package com.project.gallery.utils

import android.app.RecoverableSecurityException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.project.gallery.R
import com.project.gallery.models.FileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

fun shareImageFile(model: FileModel, context: Context) {
    val file = File(model.path)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    Intent(Intent.ACTION_SEND).also {
        it.type = "image/*"
        it.putExtra(Intent.EXTRA_STREAM, uri)
        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(it, context.getString(R.string.share_image)))
    }
}

suspend fun deleteFile(model: FileModel, context: Context): IntentSender? {
    return withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        try {
            context.contentResolver.delete(model.uri, null, null)
            null
        } catch (e: SecurityException) {
               when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    MediaStore.createDeleteRequest(contentResolver, listOf(model.uri)).intentSender
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    val recoverableSecurityException = e as? RecoverableSecurityException
                    recoverableSecurityException?.userAction?.actionIntent?.intentSender
                }

                else -> {
                    null
                }
            }
        }

    }
}