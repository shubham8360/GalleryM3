package com.project.gallery.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.project.gallery.models.FileModel
import java.io.File

fun shareImageFile(model: FileModel, context:Context){
    val file=File(model.path)
    val uri=FileProvider.getUriForFile(context,"${context.packageName}.provider",file)

    Intent(Intent.ACTION_SEND).also {
        it.type="image/*"
        it.putExtra(Intent.EXTRA_STREAM,uri)
        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(it,"Share Image"))
    }
}