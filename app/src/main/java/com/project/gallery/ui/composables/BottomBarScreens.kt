package com.project.gallery.ui.composables

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.project.gallery.R


sealed class BottomBarScreens(
    val route: String,
    @StringRes val tittle: Int,
    val icon: ImageVector
) {
    object HomeScreen : BottomBarScreens(
        route = Route.HOME_SCREEN,
        tittle = R.string.images,
        icon = Icons.Default.Home
    )

    object VideoScreen : BottomBarScreens(
        route = Route.VIDEO_SCREEN,
        tittle = R.string.videos,
        icon = Icons.Default.Edit
    )

}