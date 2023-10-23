package pt.cm.faturasua.components

sealed class DropdownMenuClass(
    val route :String,
    val title : String
) {
    object Profile: DropdownMenuClass(
        route = "profile",
        title = "profile"
    )

    object Settings: DropdownMenuClass(
        route = "settings",
        title = "settings"
    )

    object LogOut : DropdownMenuClass(
        route = "logout",
        title = "logout"
    )
}