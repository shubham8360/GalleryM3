package com.project.gallery.vm

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.project.gallery.models.FileModel
import com.project.gallery.models.Folder
import com.project.gallery.utils.Constants
import com.project.gallery.utils.Constants.PHONE_IMAGES
import com.project.gallery.utils.Constants.SHARED_PREFERENCES
import com.project.gallery.utils.Constants.THEME_VALUE
import com.project.gallery.utils.PermissionManager
import com.project.gallery.utils.StorageUtils
import com.project.gallery.utils.StorageUtils.getAllFiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val app: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {


    /* val videoList =
         savedStateHandle.getStateFlow(PHONE_VIDEOS, HashMap<String, ArrayList<FileModel>>())*/


    private val sharedPreferences by lazy {
        app.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
    }
    var folderList = mutableStateListOf<Folder>()
    val allImagesList =
        savedStateHandle.getStateFlow(PHONE_IMAGES, emptyList<FileModel>())

    var isDarkThemeEnabled =savedStateHandle.getLiveData(THEME_VALUE,sharedPreferences.getBoolean(THEME_VALUE,false))
    fun setDarkTheme(enableDark: Boolean) {
        sharedPreferences.edit { putBoolean(THEME_VALUE, enableDark) }
        savedStateHandle[THEME_VALUE]=enableDark
    }

    init {
        if (PermissionManager.isPermission(app, Constants.PERMISSIONS)) {
            scanImages()
        }
    }


    private fun scanVideos() {
        viewModelScope.launch() {
            /*    val videosHashmap = app.getAllFiles(StorageUtils.Keys.VIDEO)
                savedStateHandle[PHONE_VIDEOS] = videosHashmap*/
        }
    }

    fun scanImages() {
        viewModelScope.launch {
            val hashMap = app.getAllFiles(StorageUtils.Keys.IMAGES)
            if (!folderList.isEmpty()) folderList.clear()
            folderList.addAll(hashMap.entries.toList().map { Folder(it.key, it.value) })
            savedStateHandle[PHONE_IMAGES] =
                hashMap.entries.toList().flatMap { it.value }.sortedByDescending { it.modifiedDate }
        }
    }

}
