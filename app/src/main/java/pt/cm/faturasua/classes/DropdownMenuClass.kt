package pt.cm.faturasua.classes

sealed class DropdownMenuClass(
    val route :String,
    val title : String
) {
    object Profile: DropdownMenuClass(
        route = "profile",
        title = "Profile"
    )

    object Settings: DropdownMenuClass(
        route = "settings",
        title = "Settings"
    )

}