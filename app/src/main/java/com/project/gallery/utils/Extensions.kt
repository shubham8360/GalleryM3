package com.project.gallery.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun Any.io(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO).launch(block = block)
}
fun Any.default(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Default).launch(block = block)
}

fun Any.main(block: CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch(block = block)
}