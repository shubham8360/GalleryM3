package com.project.gallery.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.gallery.models.FileModel

@Database(entities = [FileModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class FileDatabase : RoomDatabase() {
    abstract fun getDao(): FileDao
}