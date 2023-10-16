package pt.cm.faturasua.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.cm.faturasua.components.BottomBarClass

@Composable
fun BottomBarNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarClass.History.route
    ) {
        composable(BottomBarClass.History.route){

        }
        composable(BottomBarClass.Scan.route){

        }
        composable(BottomBarClass.Statistics.route){

        }
        composable(BottomBarClass.Maps.route){

        }
    }
}