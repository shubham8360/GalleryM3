package com.project.gallery.ui.composables

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.project.gallery.models.FileModel
import com.project.gallery.vm.MainViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FolderImages(
    viewModel: MainViewModel,
    folderName: String,
    onImageClick: (bucketName:String,id: Long) -> Unit
) {

    val foldersList = viewModel.folderList
    val folderContent=foldersList.filter { it.name==folderName}.flatMap {
        it.content
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = folderName)
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
                                onImageClick(folderName,image.fileId)
                            }, image = image,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

    }

}