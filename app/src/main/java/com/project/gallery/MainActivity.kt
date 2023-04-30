package com.project.gallery

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.project.gallery.ui.composables.MainScreen
import com.project.gallery.ui.theme.GalleryM3Theme
import com.project.gallery.utils.Constants.PERMISSIONS
import com.project.gallery.utils.PermissionManager.isPermission
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
               MainScreen()
            }
        }
    }

}
