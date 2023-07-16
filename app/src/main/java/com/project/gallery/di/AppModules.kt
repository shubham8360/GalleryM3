package com.project.gallery.di

import android.content.Context
import androidx.room.Room
import com.project.gallery.db.FileDao
import com.project.gallery.db.FileDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    private const val DATABASE = "file_db"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FileDatabase {
     return Room.databaseBuilder(context, FileDatabase::class.java, DATABASE).build()
    }
    @Provides
    @Singleton
    fun getDao(db:FileDatabase): FileDao =db.getDao()

}