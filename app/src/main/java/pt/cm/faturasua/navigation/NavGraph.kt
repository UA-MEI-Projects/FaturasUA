package pt.cm.faturasua.navigation

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.cm.faturasua.classes.BottomBarClass
import pt.cm.faturasua.classes.DropdownMenuClass
import pt.cm.faturasua.classes.ScanFABItemClass
import pt.cm.faturasua.screens.DashboardScreen
import pt.cm.faturasua.screens.HistoryScreen
import pt.cm.faturasua.screens.ProfileScreen
import pt.cm.faturasua.screens.ScanScreen
import pt.cm.faturasua.screens.SettingsScreen
import pt.cm.faturasua.screens.StatisticsScreen
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    context: Context,
    firebaseUtil: FirebaseUtil,
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarClass.Dashboard.route,
        modifier = modifier
    ) {
        composable(BottomBarClass.History.route){
            HistoryScreen(userViewModel = userViewModel, firebaseUtil = firebaseUtil)
        }
        composable(BottomBarClass.Dashboard.route){
            DashboardScreen(userViewModel = userViewModel)
        }
        composable(BottomBarClass.Statistics.route){
            StatisticsScreen(userViewModel = userViewModel)
        }
        composable(DropdownMenuClass.Profile.route){
            ProfileScreen(firebaseUtil = firebaseUtil, userViewModel = userViewModel)
        }
        composable(DropdownMenuClass.Settings.route){
            SettingsScreen(context, userViewModel = userViewModel)
        }
        composable(ScanFABItemClass.Scan.route){
            ScanScreen(userViewModel = userViewModel, firebaseUtil = firebaseUtil)
        }
    }
}