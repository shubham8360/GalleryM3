package com.project.gallery.utils

import android.content.Context
import com.project.gallery.db.FileDao
import com.project.gallery.models.FileModel
import com.project.gallery.models.Folder
import com.project.gallery.utils.StorageUtils.getAllFiles
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class Repository @Inject constructor(
    @ApplicationContext val context: Context,
    private val dao: FileDao
) : FileDao {

    override fun insertFiles(list: List<FileModel>) = dao.insertFiles(list)

    fun getFolder(isVideo: Boolean = false): Flow<List<Folder>> {
        return getFiles(isVideo)
            .map { fileModel ->
                fileModel.groupBy { it.bucketName }.map { list ->
                    val items = list.value.sortedByDescending { it.modifiedDate }
                    Folder(list.key ?: "", items)
                }
            }.flowOn(Dispatchers.IO)


    }

    override suspend fun deleteFile(list: List<FileModel>) = dao.deleteFile(list)

    override fun getFiles(isVideo: Boolean): Flow<List<FileModel>> {
        return dao.getFiles(isVideo)
            .map { fileModels ->
                val filePaths = fileModels.map { it.path }
                val existingFilePaths = filePaths.filter { File(it).exists() }

                val nonExistentFileModels = fileModels.filterNot { it.path in existingFilePaths }

                if (nonExistentFileModels.isNotEmpty()) {
                    deleteFile(nonExistentFileModels)
                }

                fileModels.filter { it.path in existingFilePaths }
            }.flowOn(Dispatchers.IO)
    }


    suspend fun scanImages(list: List<FileModel>) {
        withContext(Dispatchers.IO) {
            val imageMap = context.getAllFiles(StorageUtils.Keys.IMAGES)
            val files = imageMap.entries.flatMap { it.value }

            val itemsAdded = files.filterNot { it in list }
            val removedItems = list.filterNot { it in files }


            if (itemsAdded.isNotEmpty()) {
                dao.insertFiles(itemsAdded)
            }

            if (removedItems.isNotEmpty()) {
                dao.deleteFile(removedItems)
            }
        }
    }

    suspend fun scanVideos(list: List<FileModel>) {
        val videoMap = context.getAllFiles(StorageUtils.Keys.VIDEO)
        val files2 = videoMap.entries.flatMap { it.value }
        insertFiles(files2)
    }


}