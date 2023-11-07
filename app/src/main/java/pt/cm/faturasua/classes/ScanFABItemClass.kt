package pt.cm.faturasua.classes

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