package pt.cm.faturasua.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    val receiptsList = userViewModel.receiptsList.collectAsState().value
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
        Column(
            modifier = Modifier.padding(it)
        ) {
            LazyColumn(
                userScrollEnabled = true
            ) {
                items(receiptsList){ it ->
                    InvoiceCard(
                        firebaseUtil = firebaseUtil,
                        adminMode = true,
                        type = it.type,
                        category = it.category,
                        timestamp = it.timestamp,
                        number = it.id,
                        title = it.title,
                        amount = it.amount.toDouble(),
                        date = it.date,
                        nif = it.businessNIF.toInt(),
                        iva = it.iva.toDouble(),
                        user = it.userId,
                        status = it.status
                    )
                }
            }
        }
    }
}