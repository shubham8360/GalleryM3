package com.project.gallery.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.project.gallery.models.FileModel
import com.project.gallery.models.Folder
import com.project.gallery.utils.Constants
import com.project.gallery.utils.Constants.FOLDER_LIST
import com.project.gallery.utils.Constants.PHONE_IMAGES
import com.project.gallery.utils.Constants.PHONE_VIDEOS
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

    val videoList =
        savedStateHandle.getStateFlow(PHONE_VIDEOS, HashMap<String, ArrayList<FileModel>>())
    val folderList =
        savedStateHandle.getStateFlow(FOLDER_LIST, emptyList<Folder>())
    val allImagesList =
        savedStateHandle.getStateFlow(PHONE_IMAGES, emptyList<FileModel>())


    init {
        if (PermissionManager.isPermission(app, Constants.PERMISSIONS)) {
            onPermission()
        }
    }

    fun onPermission() {
//        scanVideos()
        scanImages()
    }


    private fun scanVideos() {
        viewModelScope.launch() {
            val videosHashmap = app.getAllFiles(StorageUtils.Keys.VIDEO)
            savedStateHandle[PHONE_VIDEOS] = videosHashmap
        }
    }

    private fun scanImages() {
        viewModelScope.launch {
            val hashMap = app.getAllFiles(StorageUtils.Keys.IMAGES)
            savedStateHandle[FOLDER_LIST] = hashMap.entries.toList().map { Folder(it.key, it.value) }
            savedStateHandle[PHONE_IMAGES] =
                hashMap.entries.toList().flatMap { it.value }.sortedByDescending { it.modifiedDate }
        }
    }

}
