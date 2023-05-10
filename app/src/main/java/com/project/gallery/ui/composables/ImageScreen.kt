package com.project.gallery.ui.composables

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.project.gallery.R
import com.project.gallery.models.FileModel
import com.project.gallery.vm.MainViewModel


@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onImageClick: (image: Long) -> Unit,
    onMoreClick: (name: String) -> Unit
) {
    val folderList by viewModel.folderList.collectAsStateWithLifecycle()

    LazyColumn(modifier = modifier) {
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
                        ImageItem(
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .clickable {
                                    onImageClick(image.fileId)
                                }, image = image,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

        }
    }

}


@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    image: FileModel,
    contentScale: ContentScale
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image.path)
            .crossfade(true)
            .build()
    )

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(3.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.cd_gallery_photo),
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
