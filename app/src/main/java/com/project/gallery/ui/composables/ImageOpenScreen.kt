package com.project.gallery.ui.composables

import android.util.Log
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.project.gallery.utils.Constants.ID_CONST
import com.project.gallery.vm.MainViewModel

private const val TAG = "ImageOpenScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageOpenedScreen(navController: NavHostController, viewModel: MainViewModel) {
    val currentBackStackEntry = navController.currentBackStackEntry
    val argument = currentBackStackEntry?.arguments
    val customData = argument?.getLong(ID_CONST)

    val imageListState by viewModel.imageList.collectAsStateWithLifecycle()

    val list = imageListState.flatMap {
        it.value
    }

    val locateItem = list.indexOf(list.find { it.fileId == customData })
    var currentIndex by remember {
        mutableStateOf(locateItem)
    }
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = 100f
    var isSwiping by remember { mutableStateOf(false) }

    val onSwipeLeft = {
        if (currentIndex > 0) {
            currentIndex--
        }
    }
    val onRightSwipe = {
        if (currentIndex < list.size - 1) {
            currentIndex++
        }
    }

    var openMenu by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { openMenu = !openMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
                DropdownMenu(expanded = openMenu, onDismissRequest = { openMenu = !openMenu }) {
                    DropdownMenuItem(text = { Text(text = "Details") }, onClick = { /*TODO*/ })
                    DropdownMenuItem(text = { Text(text = "Share") }, onClick = { /*TODO*/ })
                    DropdownMenuItem(text = { Text(text = "Delete") }, onClick = { /*TODO*/ })
                    DropdownMenuItem(text = { Text(text = "Crop") }, onClick = { /*TODO*/ })
                }
            }
        )
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageItem(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            if (!isSwiping) {
                                offsetX += dragAmount.x
                                when {
                                    offsetX < -swipeThreshold -> {
                                        //swipeLeft
                                        isSwiping = true
                                        Log.d(TAG, "ImageOpenedScreen:left ")
                                        onSwipeLeft.invoke()
                                        offsetX = 0f
                                        isSwiping = false
                                    }

                                    offsetX > swipeThreshold -> {
                                        //swipeRight
                                        isSwiping = true
                                        Log.d(TAG, "ImageOpenedScreen: right")
                                        onRightSwipe.invoke()
                                        offsetX = 0f
                                        isSwiping = false
                                    }
                                }
                            }
                        }
                    }
                    .fillMaxSize(),
                image = list[currentIndex],
                contentScale = ContentScale.Fit
            )
        }
    }


}

