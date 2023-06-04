package com.project.gallery.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.gallery.R
import com.project.gallery.ui.composables.bottom_nav.BottomBarScreens
import com.project.gallery.ui.composables.bottom_nav.ImageScreen
import com.project.gallery.ui.composables.bottom_nav.VideoScreenMain
import com.project.gallery.utils.AppIcon
import com.project.gallery.utils.Constants
import com.project.gallery.utils.PermissionManager.isPermission
import com.project.gallery.vm.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onImageClick: (bucketName:String,id: Long) -> Unit,
    onMoreClick: (name: String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val permission by remember {
            mutableStateOf(isPermission(context, Constants.PERMISSIONS))
        }
        val isDarkTheme by viewModel.isDarkThemeEnabled.observeAsState()

        if (permission) {
            viewModel.scanImages()
        }
        val scrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MediumTopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(text = context.getString(R.string.photos))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    context.getString(R.string.not_implemented),
                                    "Action",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = context.getString(R.string.back_button_cd)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                isDarkTheme?.let {
                                    viewModel.setDarkTheme(!it)
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = if (isDarkTheme == true) AppIcon.Night else AppIcon.Day),
                                contentDescription = null
                            )
                        }
                    }
                )
            }, snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            bottomBar = {
                NavigationBar {
                    val screens = listOf(
                        BottomBarScreens.HomeScreen,
                        BottomBarScreens.VideoScreen
                    )
                    val currentDestination = navBackStackEntry?.destination

                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = screen.icon),
                                    contentDescription = null
                                )
                            },
                            label = { Text(text = stringResource(screen.tittle)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            })
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomBarScreens.HomeScreen.route,
            ) {
                composable(BottomBarScreens.HomeScreen.route) {
                    ImageScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        onImageClick = { bucket,id ->
                            onImageClick.invoke(bucket,id)
                        },
                        onMoreClick = { folderName ->
                            onMoreClick.invoke(folderName)
                        })
                }
                composable(BottomBarScreens.VideoScreen.route) {
                    VideoScreenMain()
                }
            }
        }

    }
}


