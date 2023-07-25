package com.project.gallery.models

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigInteger


@Entity
@Parcelize
data class FileModel(
    @PrimaryKey
    val fileId: BigInteger,
    val path: String,
    val name: String?,
    val size: BigInteger?,
    val modifiedDate: Long?,
    val duration: Int?,
    val bucketId: BigInteger?,
    val bucketPath: String?,
    val bucketName: String?,
    val uri: Uri,
    var isVideo: Boolean = false
) : Parcelable

fun FileModel.exists(context:Context):Boolean  {
    val projection = arrayOf(MediaStore.MediaColumns.DATA)
    val selection = "${MediaStore.MediaColumns._ID} = ?"
    val selectionArgs = arrayOf(uri.lastPathSegment)
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )

    cursor?.use { c ->
        return c.moveToFirst()
    }

    return false
}
