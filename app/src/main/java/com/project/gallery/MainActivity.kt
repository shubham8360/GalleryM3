package com.project.gallery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.gallery.ui.composables.FolderImages
import com.project.gallery.ui.composables.ImageOpenedScreen
import com.project.gallery.ui.composables.MainScreen
import com.project.gallery.ui.composables.Route
import com.project.gallery.ui.theme.GalleryM3Theme
import com.project.gallery.utils.Constants.ID_CONST
import com.project.gallery.utils.Constants.NAME_CONST
import com.project.gallery.utils.Constants.PERMISSIONS
import com.project.gallery.utils.PermissionManager.isPermission
import com.project.gallery.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isGranted = true
            it.entries.forEach { entry ->
                if (!entry.value) {
                    isGranted = entry.value
                    return@forEach
                }
            }
            if (!isGranted)
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                    .show()

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isPermission(this, PERMISSIONS)) requestLauncher.launch(PERMISSIONS)
        setContent {
            GalleryM3Theme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()
                NavHost(navController = navController, startDestination = Route.HOME_SCREEN) {
                    composable(route = Route.HOME_SCREEN) {
                        MainScreen(viewModel, onImageClick = {
                            navController.navigate(Route.IMAGE_OPEN_SCREEN + "/$it")
                        }, onMoreClick = { folderName ->
                            navController.navigate(Route.FOLDER_OPEN_SCREEN + "/$folderName")
                        })
                    }
                    composable(Route.IMAGE_OPEN_SCREEN + "/{$ID_CONST}", arguments = listOf(
                        navArgument(ID_CONST) {
                            type = NavType.LongType
                            defaultValue = 1L
                        }
                    )) {
                        ImageOpenedScreen(navController, viewModel)
                    }
                    composable(
                        route = Route.FOLDER_OPEN_SCREEN + "/{$NAME_CONST}",
                        arguments = listOf(navArgument(NAME_CONST) {
                            type = NavType.StringType
                        })
                    ) {
                        val name =
                            navController.currentBackStackEntry?.arguments?.getString(NAME_CONST)
                        name?.let {
                            FolderImages(viewModel, name) { id ->
                                navController.navigate(Route.IMAGE_OPEN_SCREEN + "/$id")
                            }
                        }
                    }
                }
            }
        }
    }
}
