package com.project.gallery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.project.gallery.ui.composables.MainScreen
import com.project.gallery.ui.composables.Route
import com.project.gallery.ui.composables.image.FolderImages
import com.project.gallery.ui.composables.image.ImageOpenedScreen
import com.project.gallery.ui.composables.video.VideoOpenScreen
import com.project.gallery.ui.theme.GalleryM3Theme
import com.project.gallery.utils.Constants.FOLDER_NAME
import com.project.gallery.utils.Constants.ID_CONST
import com.project.gallery.utils.Constants.NAME_CONST
import com.project.gallery.utils.Constants.PERMISSIONS
import com.project.gallery.utils.PermissionManager.isPermission
import com.project.gallery.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel by viewModels<MainViewModel>()
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

            val isDarkMode by viewModel.isDarkThemeEnabled.observeAsState()
            GalleryM3Theme(darkTheme = isDarkMode == true) {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()

                NavHost(navController = navController, startDestination = Route.HOME_SCREEN) {
                    composable(route = Route.HOME_SCREEN) {
                        MainScreen(viewModel, onImageClick = { bucket, id ->
                            navController.navigate(Route.IMAGE_OPEN_SCREEN + "/$id" + "/$bucket")
                        }, onMoreClick = { folderName ->
                            navController.navigate(Route.FOLDER_OPEN_SCREEN + "/$folderName")
                        })
                    }
                    composable(Route.IMAGE_OPEN_SCREEN + "/{$ID_CONST}" + "/{$FOLDER_NAME}",
                        arguments = listOf(
                            navArgument(ID_CONST) {
                                type = NavType.LongType
                                defaultValue = 1L
                            },
                            navArgument(FOLDER_NAME) {
                                type = NavType.StringType
                                defaultValue = null
                                nullable = true
                            }
                        )) {
                        ImageOpenedScreen(navController, viewModel)
                    }
                    composable(Route.VIDEO_OPEN_SCREEN+"/{$ID_CONST}"+"/{$FOLDER_NAME}", arguments = listOf(
                        navArgument(ID_CONST){
                            type= NavType.LongType
                            defaultValue=1L
                        }
                    )){
                        VideoOpenScreen(viewModel = viewModel)
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
                            FolderImages(viewModel, name, onImageClick = { bucket, id ->
                                navController.navigate(Route.IMAGE_OPEN_SCREEN + "/$id" + "/$bucket")
                            }, onBackClick = {
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}
