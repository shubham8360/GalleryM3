package com.project.gallery.models

import android.net.Uri
import android.os.Parcelable
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
