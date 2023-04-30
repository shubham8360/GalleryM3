package com.project.gallery.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionManager {

    fun isPermission(context: Context, array: Array<String>): Boolean {
        var isGranted = true
        array.forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isGranted = false
            }
            if (!isGranted) return@forEach
        }
        return isGranted
    }

}