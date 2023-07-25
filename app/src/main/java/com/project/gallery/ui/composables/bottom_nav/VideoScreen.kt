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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.gallery.ui.composables.image.ImageItem
import com.project.gallery.vm.MainViewModel
import java.math.BigInteger

@Composable
fun VideoScreenMain(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onItemClick: (bucket: String, id: BigInteger) -> Unit
) {

    val list by viewModel.allVideos.collectAsState(emptyList())
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = list) {
        viewModel.tempVideoFolder = list
    }

    LazyColumn(modifier = modifier) {
        items(list) {
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
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    if (it.content.size > 10)
                        IconButton(onClick = {

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
                    items(it.content) {
                        ImageItem(
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .clickable {
                                    onItemClick.invoke(it.bucketName!!, it.fileId)
                                }, fileModel = it, contentScale = ContentScale.Crop
                        )

                    }
                }

            }
        }
    }

}