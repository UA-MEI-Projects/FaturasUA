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
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.cm.faturasua.classes.ScanFABState
import pt.cm.faturasua.classes.BottomBarClass
import pt.cm.faturasua.classes.DropdownMenuClass
import pt.cm.faturasua.components.BottomBar
import pt.cm.faturasua.components.ScanFAB
import pt.cm.faturasua.components.TopBar
import pt.cm.faturasua.navigation.NavGraph

@Composable
fun MainScreen(
    onScanSelected: () -> Unit
) {
    val navController = rememberNavController()
    var floatingActionButtonState by remember{
        mutableStateOf(ScanFABState.Collapsed)
    }

    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = { ScanFAB(
            navController = navController,
            scanFABState = floatingActionButtonState,
            onScanFabStateChange = {
                floatingActionButtonState = it
            },
            onScanSelected = onScanSelected
        )}
    ) {
        it
        NavGraph(
            navController = navController
        )
    }
}



