package com.project.gallery.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onImageClick: (image: Long) -> Unit
) {
    val imagesState by viewModel.imageList.collectAsStateWithLifecycle()
    val list = imagesState.flatMap { it.value }

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalItemSpacing = 5.dp
    ) {
        itemsIndexed(list) { index: Int, image: FileModel ->
            ImageItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (index % 2 == 0) 200.dp else 250.dp)
                    .clickable {
                        onImageClick(image.fileId)
                    }, image = image,
                contentScale = ContentScale.Crop
            )
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

    Card(modifier = modifier,
        elevation = CardDefaults.cardElevation(3.dp),
        shape = CardDefaults.elevatedShape
        ) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.cd_gallery_photo),
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
