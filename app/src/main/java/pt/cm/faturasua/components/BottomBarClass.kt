package pt.cm.faturasua.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarClass(
    val route :  String,
    val title : String,
    val icon : ImageVector
){
    object History : BottomBarClass(
        route = "history",
        title = "History",
        icon = Icons.Default.List
    )

    object Dashboard : BottomBarClass(
        route = "dashboard",
        title = "dashboard",
        icon = Icons.Default.Home
    )

    object Statistics : BottomBarClass(
        route = "statistics",
        title = "Statistics",
        icon = Icons.Default.Share
    )
}
