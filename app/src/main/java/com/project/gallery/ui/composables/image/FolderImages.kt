package com.project.gallery.ui.composables.image

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.project.gallery.R
import com.project.gallery.models.FileModel
import com.project.gallery.vm.MainViewModel
import java.math.BigInteger

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FolderImages(
    viewModel: MainViewModel,
    folderName: String,
    onImageClick: (bucketName:String,id: BigInteger) -> Unit,
    onBackClick:()->Unit
) {

    val foldersList by  viewModel.folderList.collectAsState(emptyList())

    val folderContent=foldersList.filter { it.name ==folderName}.flatMap {
        it.content
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = folderName)
        }, navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back_button_cd) )
            }
        })
    }) {
        Column (modifier = Modifier.padding(it)){
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp),
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalItemSpacing = 5.dp
            ) {

                itemsIndexed(folderContent) { index: Int, image: FileModel ->
                    ImageItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (index % 2 == 0) 200.dp else 250.dp)
                            .clickable {
                                onImageClick(folderName, image.fileId)
                            }, fileModel = image,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

    }

}