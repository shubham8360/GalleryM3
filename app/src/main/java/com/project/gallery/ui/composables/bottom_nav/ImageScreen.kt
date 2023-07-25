package com.project.gallery.ui.composables.bottom_nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.project.gallery.ui.composables.image.ImageItem
import com.project.gallery.utils.Constants
import com.project.gallery.utils.PermissionManager
import com.project.gallery.vm.MainViewModel
import java.io.File
import java.math.BigInteger


@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onImageClick: (bucketName: String, image: BigInteger) -> Unit,
    onMoreClick: (name: String) -> Unit
) {
    val context = LocalContext.current
    val imagesList by viewModel.allImagesList.collectAsState(emptyList())

    val folderList by viewModel.folderList.collectAsState(emptyList())

    val permission by remember {
        mutableStateOf(PermissionManager.isPermission(context, Constants.PERMISSIONS))
    }

    LaunchedEffect(folderList) {
        viewModel.tempFolderList = folderList
    }
    LaunchedEffect(imagesList) {
        viewModel.tempFolderList = folderList
    }
    LaunchedEffect(key1 = permission) {
        viewModel.scanImages(imagesList)
    }

    LazyColumn(modifier = modifier) {
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent",
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (imagesList.size > 10)
                        IconButton(onClick = {
//                            onMoreClick.invoke(it.name)
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                }
                LazyRow(
                    modifier = Modifier.padding(vertical = 5.dp),
                    contentPadding = PaddingValues(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(imagesList) { image ->
                        if (File(image.path).exists()) {
                            ImageItem(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clickable {
                                        onImageClick(image.bucketName!!, image.fileId)
                                    }, fileModel = image,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            LaunchedEffect(key1 = Unit) {
                                viewModel.deleteFiles(listOf(image))
                            }
                        }
                    }
                }
            }
        }
        items(folderList) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    if (it.content.size > 10)
                        IconButton(onClick = {
                            onMoreClick.invoke(it.name)
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null
                            )
                        }
                }
                LazyRow(
                    modifier = Modifier.padding(vertical = 5.dp),
                    contentPadding = PaddingValues(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(it.content) { image ->
                        if (File(image.path).exists())
                            ImageItem(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clickable {
                                        onImageClick(image.bucketName!!, image.fileId)
                                    }, fileModel = image,
                                contentScale = ContentScale.Crop
                            )
                    }
                }
            }
        }
    }
}



