package pt.cm.faturasua.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import pt.cm.faturasua.R

sealed class ScanFABItemClass(
    val iconId: Int,
    val label: String,
    val route: String
){
    object Scan : ScanFABItemClass(
        iconId = R.drawable.scan,
        label = "Scan invoice",
        route = "scanFab"
    )

    object AddImage : ScanFABItemClass(
        iconId = R.drawable.gallery,
        label = "Upload invoice",
        route = "addImageFab"
    )
}