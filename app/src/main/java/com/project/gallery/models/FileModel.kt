package com.project.gallery.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FileModel(
    val fileId: Long,
    val path: String,
    val name: String?,
    val size: Long?,
    val modifiedDate: Long?,
    val duration: Int?,
    val bucketId: Long?,
    val bucketPath: String?,
    val bucketName: String?,
    val isSelected: Boolean
) : Parcelable
