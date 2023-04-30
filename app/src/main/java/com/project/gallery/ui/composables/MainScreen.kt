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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.project.gallery.R
import com.project.gallery.utils.Constants
import com.project.gallery.utils.PermissionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val permission by remember {
            mutableStateOf(PermissionManager.isPermission(context, Constants.PERMISSIONS))
        }
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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
            }
        ) {
            if (permission) {
                ImageScreen(modifier = Modifier.padding(it))
            }
        }

    }
}