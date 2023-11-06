@file:OptIn(ExperimentalMaterial3Api::class)

package pt.cm.faturasua.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.cm.faturasua.classes.DropdownMenuClass
import pt.cm.faturasua.classes.ScanFABState
import pt.cm.faturasua.components.BottomBar
import pt.cm.faturasua.components.ScanFAB
import pt.cm.faturasua.components.TopBar
import pt.cm.faturasua.navigation.NavGraph
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.viewmodel.UserViewModel
import kotlin.reflect.KFunction0

@Composable
fun MainScreen(
    firebaseUtil: FirebaseUtil,
    onSignOut: () -> Unit,
    userViewModel: UserViewModel = viewModel()
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

    var showFAB by rememberSaveable {
        mutableStateOf(true)
    }

    when(navBackStackEntry?.destination?.route){
        "dashboard" -> {
            showBottomBar = true
            showTopBar = true
            showFAB = true
        }
        "history" -> {
            showBottomBar = true
            showTopBar = true
            showFAB = true
        }
        "statistic" -> {
            showBottomBar = true
            showTopBar = true
            showFAB = true
        }
        "profile" -> {
            showBottomBar = false
            showTopBar = true
            showFAB = false
        }
        "settings" -> {
            showBottomBar = false
            showTopBar = true
            showFAB = false
        }
        "scanFab" -> {
            showBottomBar = false
            showTopBar = true
            showFAB = false
        }
    }

    val topbarScreens = listOf(
        DropdownMenuClass.Profile,
        DropdownMenuClass.Settings
    )

    Scaffold(
        topBar = { if(showTopBar) TopBar(
            navController = navController,
            onSignOutCallback = onSignOut,
            firebaseUtil = firebaseUtil,
            screens = topbarScreens
        ) },
        bottomBar = { if(showBottomBar) BottomBar(navController = navController) },
        floatingActionButton = { if(showFAB) ScanFAB(
            navController = navController,
            context = LocalContext.current,
            firebaseUtil = firebaseUtil,
            scanFABState = floatingActionButtonState,
            onScanFabStateChange = {
                floatingActionButtonState = it
            }
        )}
    ) {
        it
        NavGraph(
            context = LocalContext.current,
            firebaseUtil = firebaseUtil,
            navController = navController,
            userViewModel = userViewModel,
            modifier = Modifier
                .padding(it)
        )
    }
}



