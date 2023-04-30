package com.project.gallery.utils

import android.Manifest
import com.project.gallery.R
import com.project.gallery.models.FileModel

object Constants {
    val PERMISSIONS =
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
    const val PHONE_VIDEOS = "phone_videos"
    const val PHONE_IMAGES="phone_images"
}