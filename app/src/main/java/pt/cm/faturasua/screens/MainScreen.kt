@file:OptIn(ExperimentalMaterial3Api::class)

package pt.cm.faturasua.screens

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.core.component.get
import pt.cm.faturasua.classes.ScanFABState
import pt.cm.faturasua.classes.BottomBarClass
import pt.cm.faturasua.classes.DropdownMenuClass
import pt.cm.faturasua.components.BottomBar
import pt.cm.faturasua.components.ScanFAB
import pt.cm.faturasua.components.TopBar
import pt.cm.faturasua.navigation.NavGraph
import pt.cm.faturasua.viewmodel.UserViewModel

@Composable
fun MainScreen(
    onScanSelected: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var floatingActionButtonState by remember{
        mutableStateOf(ScanFABState.Collapsed)
    }

    var showBottomBar by rememberSaveable {
        mutableStateOf(true)
    }
    var showTopBar by rememberSaveable {
        mutableStateOf(true)
    }

    when(navBackStackEntry?.destination?.route){
        "dashboard" -> {
            showBottomBar = true
            showTopBar = true
        }
        "history" -> {
            showBottomBar = true
            showTopBar = true
        }
        "statistic" -> {
            showBottomBar = true
            showTopBar = true
        }
        "profile" -> {
            showBottomBar = false
            showTopBar = true
        }
        "settings" -> {
            showBottomBar = false
            showTopBar = true
        }
        "scanFab" -> {
            showBottomBar = false
            showTopBar = true
        }
    }

    Scaffold(
        topBar = { if(showTopBar) TopBar(navController = navController) },
        bottomBar = { if(showBottomBar) BottomBar(navController = navController) },
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
            context = LocalContext.current,
            navController = navController,
            modifier = Modifier
                .padding(it)
        )
    }
}



