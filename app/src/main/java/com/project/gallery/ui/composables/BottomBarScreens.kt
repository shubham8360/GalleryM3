package com.project.gallery.ui.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.project.gallery.R
import com.project.gallery.utils.AppIcon


sealed class BottomBarScreens(
    val route: String,
    @StringRes val tittle: Int,
    @DrawableRes val icon: Int
) {
    object HomeScreen : BottomBarScreens(
        route = Route.HOME_SCREEN,
        tittle = R.string.images,
        icon = AppIcon.Home
    )

    object VideoScreen : BottomBarScreens(
        route = Route.VIDEO_SCREEN,
        tittle = R.string.videos,
        icon = AppIcon.Video
    )

}