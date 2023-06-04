package com.project.gallery.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.project.gallery.R
import com.project.gallery.models.FileModel

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    image: FileModel,
    contentScale: ContentScale
) {


    val model = ImageRequest.Builder(LocalContext.current)
        .data(image.path)
        .crossfade(true)
        .allowHardware(true)
        .build()

    AsyncImage(
        model = model,
        contentDescription = stringResource(R.string.cd_gallery_photo),
        modifier = modifier,
        contentScale = contentScale,
    )
}
