@file:OptIn(ExperimentalMaterial3Api::class)

package pt.cm.faturasua.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.cm.faturasua.components.BottomBarClass
import pt.cm.faturasua.navigation.BottomBarNavGraph

@Composable
fun MainScreen(onSignIn : () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar(onSignIn) },
        bottomBar = { BottomBar(navController = navController) }
    ) {
        it
        BottomBarNavGraph(
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onSignIn: () -> Unit, modifier : Modifier = Modifier) {
    var menuExpanded by remember{
        mutableStateOf(false)
    }
    TopAppBar(title = {
        Text(text = "FaturasUA", color = Color.White)
    },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded}) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = Color.White,
                    contentDescription = "Menu Options"
                )
            }

            DropdownMenu(expanded = menuExpanded, onDismissRequest = { /*TODO*/ }) {
                DropdownMenuItem(text = { "Sign In" }, onClick = {
                    onSignIn()
                })
                DropdownMenuItem(text = { "Settings" }, onClick = { /*TODO*/ })
                DropdownMenuItem(text = { "About" }, onClick = { /*TODO*/ })
            }
        },
        modifier = modifier
    )
}

@Composable
fun BottomBar(navController: NavController) {
    val screen = listOf(
        BottomBarClass.History,
        BottomBarClass.Statistics,
        BottomBarClass.Dashboard
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(

    ) {
        screen.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarClass,
    currentDestination: NavDestination?,
    navController: NavController
) {
    NavigationBarItem(
        label = {
            Text(screen.title)
        },
        icon = {
            Icon(screen.icon, screen.title)
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route)
        },
        colors = NavigationBarItemDefaults.colors()
        /*        colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = , selectedTextColor = Color.White
                ),*/
    )
}