package pt.cm.faturasua.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.cm.faturasua.components.InvoiceCard
import pt.cm.faturasua.components.TopBar
import pt.cm.faturasua.utils.FirebaseUtil
import pt.cm.faturasua.viewmodel.UserViewModel

@Composable
fun AdminScreen(
    firebaseUtil: FirebaseUtil,
    userViewModel: UserViewModel = viewModel(),
    onSignOutCallback: () -> Unit
){

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                firebaseUtil = firebaseUtil,
                onSignOutCallback = onSignOutCallback,
                screens = listOf()
            )
        }
    ) {
        it

        LazyColumn(
            contentPadding = it
        ) {
            items(userViewModel.receiptsList.value!!){ it ->
                InvoiceCard(
                    type = it.type,
                    number = it.id,
                    title = it.title,
                    amount = it.amount.toDouble(),
                    date = it.date,
                    nif = it.businessNIF.toInt(),
                    iva = it.iva.toDouble(),
                    status = it.status
                )
            }
        }
    }
}