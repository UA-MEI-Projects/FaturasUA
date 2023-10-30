package pt.cm.faturasua.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import pt.cm.faturasua.classes.DropdownMenuClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, modifier : Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var menuExpanded by remember{
        mutableStateOf(false)
    }

    val screens = listOf(
        DropdownMenuClass.Profile,
        DropdownMenuClass.Settings,
        DropdownMenuClass.LogOut
    )

    TopAppBar(title = {
        Text(text = "FaturasUA", color = Color.White)
    },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = "Menu Options"
                )
            }

            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                screens.forEach { screen ->
                    DropdownMenuItem(
                        text = { Text(text= screen.title) },
                        onClick = {
                            navController.navigate(screen.route)
                            menuExpanded = !menuExpanded
                                  },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        },
        modifier = modifier
    )
}