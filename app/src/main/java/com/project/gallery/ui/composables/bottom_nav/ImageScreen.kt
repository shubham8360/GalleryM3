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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.project.gallery.ui.composables.image.ImageItem
import com.project.gallery.vm.MainViewModel


@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onImageClick: (bucketName:String,image: Long) -> Unit,
    onMoreClick: (name: String) -> Unit
) {
    val imagesList by viewModel.allImagesList.observeAsState(emptyList())

    val folderList by  viewModel.folderList.observeAsState(emptyList())


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
                    if ((imagesList?.size ?: 0) > 10)
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
                    items(imagesList?: emptyList()) { image ->
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
        items(folderList, key = {it.name}) {
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
                    items(it.content, key = {it.fileId}) { image ->
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



