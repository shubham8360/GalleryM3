package com.project.gallery.ui.composables.image

import android.os.Build
import android.os.CancellationSignal
import android.util.Size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.project.gallery.R
import com.project.gallery.models.FileModel

@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    fileModel: FileModel,
    contentScale: ContentScale
) {

    val context = LocalContext.current


    val thumbnail = remember {
        if (fileModel.isVideo) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(
                    fileModel.uri,
                    Size(100, 100),
                    CancellationSignal()
                )
            } else {
                null
//                getVideoThumbnail(context, file.uri)
            }
        } else {
            null
        }
    }

    val model = ImageRequest.Builder(context)
        .data(if (fileModel.isVideo) thumbnail else fileModel.path)
        .crossfade(true)
        .allowHardware(true)
        .build()

    Box(
        modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = model,
            contentDescription = stringResource(R.string.cd_gallery_photo),
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
        )
        if (fileModel.isVideo)
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd).padding(3.dp)
                    .size(20.dp)
                 ,
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = null,
                tint = colorResource(
                    id = R.color.white
                )
            )

    }

}
