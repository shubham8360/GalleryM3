package com.project.gallery.ui.composables.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.project.gallery.R
import com.project.gallery.models.FileModel
import com.project.gallery.utils.loadThumbnail

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    fileModel: FileModel,
    contentScale: ContentScale
) {

    val context = LocalContext.current


    val thumbnail = remember {
        fileModel.uri.loadThumbnail(context.contentResolver, fileModel.fileId.toLong())
    }

    /*  val model = ImageRequest.Builder(context)
          .data(if (fileModel.isVideo) thumbnail else fileModel.path)
          .crossfade(true)
          .allowHardware(true)
          .build()*/
    val model = fileModel.path

    Box(
        modifier.fillMaxSize()
    ) {
        GlideImage(
            model = model, contentDescription = null, modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(5.dp)), contentScale = contentScale
        )
        /*   AsyncImage(
               model = model,
               contentDescription = stringResource(R.string.cd_gallery_photo),
               modifier = Modifier
                   .fillMaxSize()
                   .clip(RoundedCornerShape(5.dp)),
               contentScale = contentScale,
           )*/
        if (fileModel.isVideo)
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(3.dp)
                    .size(20.dp),
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = null,
                tint = colorResource(
                    id = R.color.white
                )
            )

    }

}
