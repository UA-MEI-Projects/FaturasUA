package pt.cm.faturasua.classes

import androidx.compose.ui.graphics.ImageBitmap

sealed class ScanFABItemClass(
    val icon: ImageBitmap,
    val label: String,
    val route: String
){
    object Scan : ScanFABItemClass(
        icon = ImageBitmap(),
        label = "Scan",
        route = "ScanFab"
    )

    object AddImage : ScanFABItemClass(
        icon = ImageBitmap(),
        label = "Add Image",
        route = "AddImageFab"
    )
}