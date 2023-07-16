package com.project.gallery.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.gallery.models.FileModel
import com.project.gallery.models.Folder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFiles(list: List<FileModel>)

    @Query("select * from FileModel where isVideo==:isVideo")
    fun getFiles(isVideo: Boolean = false): Flow<List<FileModel>>

    fun getFolder(isVideo: Boolean = false) = getFiles(isVideo).map { fileModel ->
        fileModel.groupBy { it.bucketName }.map { list ->
            val items = list.value.sortedByDescending { it.modifiedDate }
            Folder(list.key ?: "", items)
        }
    }

}