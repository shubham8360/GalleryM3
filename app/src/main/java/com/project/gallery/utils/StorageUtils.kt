package com.project.gallery.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.project.gallery.models.FileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

@Volatile
var folderSet = HashMap<String, ArrayList<FileModel>>()
private const val TAG = "StorageUtils"

object StorageUtils {

    object Keys {
        val IMAGES: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val VIDEO: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    suspend fun Context.getAllFiles(uri: Uri = Keys.IMAGES): HashMap<String, ArrayList<FileModel>> {
        folderSet.clear()
        return withContext(Dispatchers.IO) {
            val projection: Array<String> = if (uri == Keys.IMAGES) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    arrayOf(
                        MediaStore.MediaColumns._ID,
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        MediaStore.MediaColumns.SIZE,
                        MediaStore.MediaColumns.DATE_MODIFIED,
                        MediaStore.MediaColumns.BUCKET_ID,
                        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.MediaColumns.DATA//nov
                    )
                } else {
                    arrayOf(
                        MediaStore.MediaColumns._ID,
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        MediaStore.MediaColumns.SIZE,
                        MediaStore.MediaColumns.DATE_MODIFIED,
                        MediaStore.MediaColumns.DATA
                    )
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    arrayOf(
                        MediaStore.MediaColumns._ID,
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        MediaStore.MediaColumns.SIZE,
                        MediaStore.MediaColumns.DATE_MODIFIED,
                        MediaStore.MediaColumns.DURATION,
                        MediaStore.MediaColumns.BUCKET_ID,
                        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.MediaColumns.DATA//nov
                    )
                } else {
                    arrayOf(
                        MediaStore.MediaColumns._ID,
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        MediaStore.MediaColumns.SIZE,
                        MediaStore.MediaColumns.DATE_MODIFIED,
                        MediaStore.MediaColumns.DATA
                    )
                }
            }
            getFilesList(projection, uri)
        }
    }

    fun log(string: String) {
        Log.i("StorageUtils", "log: $string")
    }

    private suspend fun Context.getFilesList(
        projection: Array<String>,
        pUri: Uri
    ): HashMap<String, ArrayList<FileModel>> {
        log("getVideosList")
        return withContext(Dispatchers.IO) {
            val selection = null
            val selectionArgs = null
            val sortOrder: String = MediaStore.Audio.Media.DISPLAY_NAME + " DESC"
            val filesList = ArrayList<FileModel>()
            var folderName = ""

            contentResolver.query(pUri, projection, selection, selectionArgs, sortOrder)
                ?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val id =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                        var name =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                        val modifiedDate =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
                        val size =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE))
                        var duration: Int? = null

                        var bucketId: Long? = null
                        var bucketPath: String? = null
                        var bucketName: String? = null

                        val path =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (pUri == Keys.VIDEO) duration =
                                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION))
                            bucketId =
                                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID))
                            bucketName =
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME))
                        }//nov else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            if (name == null || !name.contains(".")) name =
                                path?.substring(path.lastIndexOf("/") + 1)
                            //if (path != null) size = File(path).length()
                            bucketPath = path?.substring(0, path.lastIndexOf("/"))
                            bucketName = bucketPath?.substring(bucketPath.lastIndexOf("/") + 1)
                        }

                        if (folderName == "") {
                            folderName = bucketName.toString()
                            filesList.clear()
                            filesList.add(
                                FileModel(
                                    fileId = id,
                                    path = path,
                                    name = name,
                                    size = size,
                                    modifiedDate = modifiedDate,
                                    duration = duration,
                                    bucketId = bucketId,
                                    bucketPath = bucketPath,
                                    bucketName = bucketName,
                                    isSelected = false
                                )
                            )
                            val arrayList = ArrayList<FileModel>()
                            arrayList.addAll(filesList)
                            folderSet[bucketName.toString()] = arrayList
                        } else {
                            val oldSelectedFolder = folderName
                            folderName = bucketName.toString()

                            if (oldSelectedFolder == folderName) {
                              /*  Log.d(
                                    TAG,
                                    "getFilesList: id=$id, path=$path,name=$path,size=$size,modified date=$modifiedDate,duration-=$duration,bucketId=$bucketId,bucketPath=$bucketPath,bucketname=$bucketName"
                                )*/
                                filesList.add(
                                    FileModel(
                                        fileId = id,
                                        path = path,
                                        name = name,
                                        size = size,
                                        modifiedDate = modifiedDate,
                                        duration = duration,
                                        bucketId = bucketId,
                                        bucketPath = bucketPath,
                                        bucketName = bucketName,
                                        isSelected = false
                                    )
                                )
                                val arrayList = ArrayList<FileModel>()
                                arrayList.addAll(filesList)
                                folderSet[bucketName.toString()] = arrayList
                            } else {
                                folderName = bucketName.toString()
                                filesList.clear()
                                val oldList = folderSet.get(folderName)
                                if (oldList != null && oldList.isNotEmpty()) filesList.addAll(
                                    oldList
                                )
                                filesList.add(
                                    FileModel(
                                        fileId = id,
                                        path = path,
                                        name = name,
                                        size = size,
                                        modifiedDate = modifiedDate,
                                        duration = duration,
                                        bucketId = bucketId,
                                        bucketPath = bucketPath,
                                        bucketName = bucketName,
                                        isSelected = false
                                    )
                                )
                                val arrayList = ArrayList<FileModel>()
                                arrayList.addAll(filesList)
                                folderSet[bucketName.toString()] = arrayList
                            }

                        }

                    }
                }
            folderSet
        }
    }

}


fun <T> Any.getAnyPrivateMemberField(memberName: String): T? {
    val field = this.javaClass.getDeclaredField(memberName)
    field.isAccessible = true
    @Suppress("UNCHECKED_CAST") return (field.get(this) as? T)
}