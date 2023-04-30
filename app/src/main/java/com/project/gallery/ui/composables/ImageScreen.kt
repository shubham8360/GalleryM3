package com.project.gallery.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.project.gallery.R
import com.project.gallery.models.FileModel
import com.project.gallery.vm.MainViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageScreen(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = hiltViewModel()
    val imagesState by viewModel.imageList.collectAsState()
    val list = imagesState.flatMap { it.value }
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
    ) {
        items(list) { images ->
            ImageItem(modifier = Modifier.clickable {}, image = images)
        }
        item(imagesState.keys.toList()) {}
    }

}


@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    image: FileModel
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image.path)
            .crossfade(true)
            .build()
    )

    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.cd_gallery_photo),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
    }
}
