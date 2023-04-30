package com.project.gallery.models


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
)
