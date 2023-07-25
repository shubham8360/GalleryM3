package com.project.gallery.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.gallery.models.FileModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFiles(list: List<FileModel>)

    @Query("select * from FileModel where isVideo==:isVideo ")
    fun getFiles(isVideo: Boolean = false): Flow<List<FileModel>>



    @Delete
    suspend fun deleteFile(list: List<FileModel>)

}