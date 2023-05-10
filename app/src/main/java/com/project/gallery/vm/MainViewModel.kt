package com.project.gallery.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.project.gallery.models.FileModel
import com.project.gallery.models.Folder
import com.project.gallery.utils.Constants.PHONE_IMAGES
import com.project.gallery.utils.Constants.PHONE_VIDEOS
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
        savedStateHandle.getStateFlow(PHONE_IMAGES, emptyList<Folder>())


    init {
        onPermission()
    }

    private fun onPermission() {
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
            val list = app.getAllFiles(StorageUtils.Keys.IMAGES)
            savedStateHandle[PHONE_IMAGES] = list
        }
    }

}
