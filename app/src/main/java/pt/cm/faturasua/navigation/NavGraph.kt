package pt.cm.faturasua.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.cm.faturasua.classes.BottomBarClass
import pt.cm.faturasua.classes.DropdownMenuClass
import pt.cm.faturasua.screens.DashboardScreen
import pt.cm.faturasua.screens.HistoryScreen
import pt.cm.faturasua.screens.ProfileScreen
import pt.cm.faturasua.screens.SettingsScreen
import pt.cm.faturasua.screens.StatisticsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarClass.History.route
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
            SettingsScreen()
        }
    }
}