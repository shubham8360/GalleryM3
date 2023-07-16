package com.project.gallery.utils

import android.content.Context
import com.project.gallery.db.FileDao
import com.project.gallery.models.FileModel
import com.project.gallery.models.Folder
import com.project.gallery.utils.StorageUtils.getAllFiles
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    @ApplicationContext val context: Context,
    private val dao: FileDao
) : FileDao {

    override fun insertFiles(list: List<FileModel>) = dao.insertFiles(list)

    override fun getFiles(isVideo: Boolean): Flow<List<FileModel>> = dao.getFiles(isVideo)
    override fun getFolder(isVideo: Boolean): Flow<List<Folder>> = dao.getFolder(isVideo)

    suspend fun scanImages() {
        withContext(Dispatchers.IO) {
            delay(5000)
            val imageMap = context.getAllFiles(StorageUtils.Keys.IMAGES)
            val files = imageMap.entries.flatMap { it.value }
            insertFiles(files)
            val videoMap = context.getAllFiles(StorageUtils.Keys.VIDEO)
            val files2 = videoMap.entries.flatMap { it.value }
            insertFiles(files2)
        }
    }


}