package com.project.gallery.ui.composables.image

import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.gallery.R
import com.project.gallery.utils.Constants.FOLDER_NAME
import com.project.gallery.utils.Constants.ID_CONST
import com.project.gallery.utils.deleteFile
import com.project.gallery.utils.shareImageFile
import com.project.gallery.vm.MainViewModel
import kotlinx.coroutines.launch

private const val TAG = "ImageOpenScreen"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImageOpenedScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val args = navController.currentBackStackEntry?.arguments
    val imageId = args?.getLong(ID_CONST)
    val bucketName = args?.getString(FOLDER_NAME)
    val context = LocalContext.current


    val scope = rememberCoroutineScope()

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == RESULT_OK) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                viewModel.scanImages(viewModel.tempAllImageList)
            }
        }
    val list2 = viewModel.tempFolderList

    val specificDir = list2.find { it.name == bucketName }?.content ?: emptyList()


    val locateItem = specificDir.indexOf(specificDir.find { it.fileId == imageId?.toBigInteger() })
        .takeIf { it != -1 } ?: 0
    val currentIndex by remember {
        mutableStateOf(locateItem)
    }


    var openMenu by remember {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState(initialPage = currentIndex)


    Scaffold(topBar = {
        TopAppBar(
            title = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = { openMenu = !openMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
                DropdownMenu(expanded = openMenu, onDismissRequest = { openMenu = !openMenu }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.details)) },
                        onClick = { openMenu = !openMenu })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.share)) },
                        onClick = {
                            scope.launch {
                                shareImageFile(specificDir[pagerState.currentPage], context)
                                openMenu = !openMenu
                            }
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.delete)) },
                        onClick = {
                            scope.launch {

                                val intentSender = deleteFile(
                                    specificDir[currentIndex],
                                    context
                                )
                                launcher.launch(
                                    IntentSenderRequest.Builder(intentSender!!).build()
                                )
                                openMenu = !openMenu
                            }
                        })
                }
            }
        )
    }) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            pageCount = specificDir.size,
            state = pagerState
        ) { page ->

            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val imageSize by animateFloatAsState(
                targetValue = if (pageOffset != 0.0f) 0.75f else 1f,
                animationSpec = tween(300)
            )


            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    ImageItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                            .clip(RoundedCornerShape(16.dp))
                            .graphicsLayer {
                                scaleX = imageSize
                                scaleY = imageSize
                            },
                        fileModel = specificDir[page],
                        contentScale = ContentScale.Fit
                    )

            }
            Log.d(TAG, "ImageOpenedScreen:$page ${pagerState.currentPage}")
        }
    }


}




