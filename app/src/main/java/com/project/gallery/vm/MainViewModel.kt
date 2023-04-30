package com.project.gallery.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.project.gallery.models.FileModel
import com.project.gallery.utils.Constants.PERMISSIONS
import com.project.gallery.utils.Constants.PHONE_IMAGES
import com.project.gallery.utils.Constants.PHONE_VIDEOS
import com.project.gallery.utils.PermissionManager.isPermission
import com.project.gallery.utils.StorageUtils
import com.project.gallery.utils.StorageUtils.getAllFiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val app: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    val videoList =
        savedStateHandle.getStateFlow(PHONE_VIDEOS, HashMap<String, ArrayList<FileModel>>())
    val imageList =
        savedStateHandle.getStateFlow(PHONE_IMAGES, HashMap<String, ArrayList<FileModel>>())


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
        viewModelScope.launch(Dispatchers.IO) {
            val imagesHashmap = app.getAllFiles(StorageUtils.Keys.IMAGES)
            savedStateHandle[PHONE_IMAGES] = imagesHashmap
        }
    }

}
