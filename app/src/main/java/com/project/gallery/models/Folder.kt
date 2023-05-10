package com.project.gallery.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(val name: String, val content: List<FileModel>) : Parcelable