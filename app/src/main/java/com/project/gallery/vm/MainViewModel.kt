package com.project.gallery.vm

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.project.gallery.models.FileModel
import com.project.gallery.models.Folder
import com.project.gallery.utils.Constants.PERMISSIONS
import com.project.gallery.utils.Constants.SHARED_PREFERENCES
import com.project.gallery.utils.Constants.THEME_VALUE
import com.project.gallery.utils.PermissionManager.isPermission
import com.project.gallery.utils.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val app: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repo: Repository
) : AndroidViewModel(app) {


    private val sharedPreferences by lazy {
        app.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
    }

    var folderList: Flow<List<Folder>> = repo.getFolder()
    var tempFolderList = listOf<Folder>()

    val allImagesList = repo.getFiles()
    val tempAllImageList = listOf<FileModel>()


    var allVideos = repo.getFolder(true)
    var tempVideoFolder = listOf<Folder>()

    var isDarkThemeEnabled =
        savedStateHandle.getLiveData(THEME_VALUE, sharedPreferences.getBoolean(THEME_VALUE, false))

    fun setDarkTheme(enableDark: Boolean) {
        sharedPreferences.edit { putBoolean(THEME_VALUE, enableDark) }
        savedStateHandle[THEME_VALUE] = enableDark
    }

    init {
        if (isPermission(app, PERMISSIONS)) {
            scanVideos()
        }
    }


    fun scanVideos() {
        /*viewModelScope.launch() {
            val videosHashmap = app.getAllFiles(StorageUtils.Keys.VIDEO)
            allVideos.postValue(videosHashmap.map { Folder(name = it.key, content = it.value) })
        }*/
    }

    fun scanImages(list: List<FileModel>) {
        viewModelScope.launch(IO) {
            repo.scanImages(list)
        }
        /*  val hashMap = app.getAllFiles(StorageUtils.Keys.IMAGES)
          folderList.postValue(hashMap.entries.toList().map { Folder(name = it.key, content = it.value) })
          allImagesList.postValue(hashMap.entries.toList().flatMap { it.value }
              .sortedByDescending { it.modifiedDate?:0 })
      }*/
    }

    fun deleteFiles(list: List<FileModel>) {
        viewModelScope.launch {
            repo.deleteFile(list = list)
        }
    }


}
