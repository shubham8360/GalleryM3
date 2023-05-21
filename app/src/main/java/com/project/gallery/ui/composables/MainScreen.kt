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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.gallery.R
import com.project.gallery.utils.Constants
import com.project.gallery.utils.PermissionManager
import com.project.gallery.vm.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onImageClick: (id: Long) -> Unit,
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
            mutableStateOf(PermissionManager.isPermission(context, Constants.PERMISSIONS))
        }

        if (permission) {
            viewModel.onPermission()
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
                    }
                )
            }, snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            bottomBar = {
                NavigationBar() {

                    val screens = listOf(
                        BottomBarScreens.HomeScreen,
                        BottomBarScreens.VideoScreen
                    )
                    val currentDestination = navBackStackEntry?.destination

                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon },
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
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomBarScreens.HomeScreen.route) {
                    ImageScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        onImageClick = { id ->
                            onImageClick.invoke(id)
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


