package pt.cm.faturasua.navigation

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import org.koin.android.ext.android.get
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
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarClass.Dashboard.route,
        modifier = modifier
    ) {
        composable(BottomBarClass.History.route){
            HistoryScreen()
        }
        composable(BottomBarClass.Dashboard.route){
            DashboardScreen()
        }
        composable(BottomBarClass.Statistics.route){
            StatisticsScreen()
        }
        composable(DropdownMenuClass.Profile.route){
            ProfileScreen()
        }
        composable(DropdownMenuClass.Settings.route){
            SettingsScreen(context)
        }
        composable(ScanFABItemClass.Scan.route){
            ScanScreen()
        }
    }
}